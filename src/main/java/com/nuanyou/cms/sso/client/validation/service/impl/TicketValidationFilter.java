
package com.nuanyou.cms.sso.client.validation.service.impl;


import com.nuanyou.cms.sso.client.util.AbstractFilter;
import com.nuanyou.cms.sso.client.util.CommonUtils;
import com.nuanyou.cms.sso.client.validation.exception.TicketValidationException;
import com.nuanyou.cms.sso.client.validation.service.SsoValidatorService;
import com.nuanyou.cms.sso.client.validation.service.TicketStateService;
import com.nuanyou.cms.sso.client.validation.vo.StateTicket;
import com.nuanyou.cms.sso.client.validation.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import static com.nuanyou.cms.sso.client.util.ParameterConfig.SSO_USER;

/**
 * 验证ticket的Filter
 */
@Component
public class TicketValidationFilter extends AbstractFilter {

    private static final Logger log = LoggerFactory.getLogger(TicketValidationFilter.class.getSimpleName());

    private  String validateCodeUrl;

    private  Boolean needAutoLogOut=true;

    @Autowired
    private SsoValidatorService ssoValidatorService;

    @Autowired
    private TicketStateService ticketStateService;

    public void setValidateCodeUrl(String validateCodeUrl) {
        this.validateCodeUrl = validateCodeUrl;
    }

    public void setNeedAutoLogOut(Boolean needAutoLogOut) {
        this.needAutoLogOut = needAutoLogOut;
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
        setValidateCodeUrl(getPropertyFromInitParams(filterConfig, "validateCodeUrl", null));
        super.init(filterConfig);
    }


    private static final String[] RESERVED_INIT_PARAMS = new String[]{"validateCodeUrl", "serverName", "service", "artifactParameterName", "serviceParameterName", "encodeServiceUrl", "millisBetweenCleanUps", "hostnameVerifier", "encoding", "config"};




    /**
     * 所有验证都通过且得到用户后的操作,比如写入日志
     *
     * @param request
     * @param response
     * @param user
     */
    protected void onSuccessfulValidation(final HttpServletRequest request, final HttpServletResponse response, final User user) {

    }


    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        String artifactParameterName = getTicketParameterName();
        final String ticket = CommonUtils.safeGetParameter(request, artifactParameterName);
        final String state = CommonUtils.safeGetParameter(request, "state");
        if (CommonUtils.isNotBlank(state)) {
            log.info("Second Step:state found and validate state");
            final StateTicket stateTicket = ticketStateService.getTicket(state);
            if(stateTicket==null){
                throw new ServletException("This stateTicket has been deleted,stateTicket="+state);
            }
            try {
                   if (stateTicket.isExpired()) {
                       throw new ServletException("This stateTicket has expired,stateTicket="+state);
                    }
            } catch (Exception e) {
                throw new ServletException(e);
            } finally {
                this.ticketStateService.deleteTicket(state);
            }

        }
        if (CommonUtils.isNotBlank(ticket)) {
            log.info("Second Step:Attempting to validate ticket: " + ticket);
            try {
                final User user = this.ssoValidatorService.validate(ticket, this.getServerName(),validateCodeUrl,needAutoLogOut);
                log.info("Second Step:Successfully authenticated user: " + user);
                request.setAttribute(SSO_USER, user);
                log.debug("\n" + "**************************************after validate tgt and st ********************************************");
                log.debug("put the assertion to the session scope:key:" + SSO_USER + "value:" + user);
                log.debug("assertion props" + user.toString());
                if (request.getSession(false) != null) {
                    log.debug("Session Create Time: " + new Date(request.getSession(false).getCreationTime()).toString());
                    Cookie[] cookies = request.getCookies();
                    if (cookies == null) {
                        log.debug("cookie ==null");
                    } else {
                        for (Cookie cookie : cookies) {
                            log.debug("cookie " + cookie.getName() + " = " + cookie.getValue());
                        }
                    }
                    HttpSession session = request.getSession(false);
                    Enumeration<String> enumeration = session.getAttributeNames();
                    while (enumeration.hasMoreElements()) {
                        String key = enumeration.nextElement();
                        log.debug("session " + key + " = " + session.getAttribute(key));
                    }
                } else {
                    log.debug("sessin is null");
                }
                log.debug("**************************************after validate tgt and st ********************************************" + "\n");
                request.getSession().setAttribute(SSO_USER, user);
                onSuccessfulValidation(request, response, user);
                log.debug("Redirecting after successful ticket validation.");
                response.sendRedirect(constructServiceUrl(request, response));
                return;
            } catch (final TicketValidationException e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                throw new ServletException(e);
            }
        }
        filterChain.doFilter(request, response);
    }


    public void setSsoValidatorService(SsoValidatorService ssoValidatorService) {
        this.ssoValidatorService = ssoValidatorService;
    }

}
