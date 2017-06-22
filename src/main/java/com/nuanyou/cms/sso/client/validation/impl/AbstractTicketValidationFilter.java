/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.nuanyou.cms.sso.client.validation.impl;


import com.nuanyou.cms.sso.client.util.AbstractFilter;
import com.nuanyou.cms.sso.client.util.CommonUtils;
import com.nuanyou.cms.sso.client.validation.SsoValidatorService;
import com.nuanyou.cms.sso.client.validation.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * 验证ticket的Filter
 */
@Component
public  class AbstractTicketValidationFilter extends AbstractFilter {

    private static final Logger log = LoggerFactory.getLogger(AbstractTicketValidationFilter.class.getSimpleName());



//    private TicketValidator ticketValidator;

    private SsoValidatorService ssoValidatorService;




    private boolean redirectAfterValidation = false;

    private boolean exceptionOnValidationFailure = true;

    private boolean useSession = true;

    public SsoValidatorService getSsoValidatorService() {
        return ssoValidatorService;
    }

    public void setSsoValidatorService(SsoValidatorService ssoValidatorService) {
        this.ssoValidatorService = ssoValidatorService;
    }

    protected void initInternal(final FilterConfig filterConfig) throws ServletException {
        setExceptionOnValidationFailure(parseBoolean(getPropertyFromInitParams(filterConfig, "exceptionOnValidationFailure", "true")));
        log.trace("Setting exceptionOnValidationFailure parameter: " + this.exceptionOnValidationFailure);
        setRedirectAfterValidation(parseBoolean(getPropertyFromInitParams(filterConfig, "redirectAfterValidation", "true")));
        log.trace("Setting redirectAfterValidation parameter: " + this.redirectAfterValidation);
        setUseSession(parseBoolean(getPropertyFromInitParams(filterConfig, "useSession", "true")));
        log.trace("Setting useSession parameter: " + this.useSession);
        setSsoValidatorService(getTicketValidator1(filterConfig));
        super.initInternal(filterConfig);
    }



    private static final String[] RESERVED_INIT_PARAMS = new String[] {"validateCodeUrl", "serverName", "service", "artifactParameterName", "serviceParameterName", "encodeServiceUrl", "millisBetweenCleanUps", "hostnameVerifier", "encoding", "config"};
    protected final SsoValidatorService getTicketValidator1(final FilterConfig filterConfig) {
         final String validateCodeUrl = getPropertyFromInitParams(filterConfig, "validateCodeUrl", null);
        final SsoValidatorServiceImpl validator = new SsoValidatorServiceImpl(validateCodeUrl);
        validator.setEncoding(getPropertyFromInitParams(filterConfig, "encoding", null));
        final Map<String,String> additionalParameters = new HashMap<String,String>();
        final List<String> params = Arrays.asList(RESERVED_INIT_PARAMS);
        for (final Enumeration<?> e = filterConfig.getInitParameterNames(); e.hasMoreElements();) {
            final String s = (String) e.nextElement();

            if (!params.contains(s)) {
                additionalParameters.put(s, filterConfig.getInitParameter(s));
            }
        }
        validator.setCustomParameters(additionalParameters);
        return validator;
    }

    public void init() {
        super.init();
        CommonUtils.assertNotNull(this.ssoValidatorService, "ssoValidatorService cannot be null.");
    }


    /**
     * 所有验证都通过且得到用户后的操作,比如写入日志
     * @param request
     * @param response
     * @param user
     */
    protected void onSuccessfulValidation(final HttpServletRequest request, final HttpServletResponse response, final User user) {

    }


    /**
     * 验证失败后的操作
     * @param request
     * @param response
     */
    protected void onFailedValidation(final HttpServletRequest request, final HttpServletResponse response) {
        // nothing to do here.
    }

    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        String artifactParameterName = getArtifactParameterName();
        final String ticket = CommonUtils.safeGetParameter(request, artifactParameterName);
        final String state = CommonUtils.safeGetParameter(request, "state");
        /*
        if (CommonUtils.isNotBlank(state)) {
            log.info("Second Step:state found and validate state");
            final StateTicket stateTicket = (StateTicket) this.abstractTicketRegistry.getTicket(state, StateTicket.class);
            if(stateTicket==null){
                throw new ServletException("stateTicket不存在");
            }
            try {
                synchronized (stateTicket) {
                   if (stateTicket.isExpired()) {
                        log.info("stateTicket [" + state + "] has expired.");
                        throw new InvalidTicketException();
                    }
               }
            } catch (Exception e) {
                if (this.exceptionOnValidationFailure) {
                    throw new ServletException(e);
                }
            } finally {
                this.abstractTicketRegistry.deleteTicket(state);
            }

        }else{
            log.info("Second Step:ticket not found");
        }
        */
        if (CommonUtils.isNotBlank(ticket)) {
            log.info("Second Step:Attempting to validate ticket: " + ticket);
            try {
                final User user = this.ssoValidatorService.validate(ticket);
                log.info("Second Step:Successfully authenticated user: " + user);
                request.setAttribute(SSO_USER, user);
                if (this.useSession) {
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
                }
                onSuccessfulValidation(request, response, user);

                if (this.redirectAfterValidation) {
                    log.debug("Redirecting after successful ticket validation.");
                    response.sendRedirect(constructServiceUrl(request, response));
                    return;
                }
            } catch (final TicketValidationException e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                onFailedValidation(request, response);

                if (this.exceptionOnValidationFailure) {
                    throw new ServletException(e);
                }
                return;
            }
        }

        filterChain.doFilter(request, response);

    }



    public final void setRedirectAfterValidation(final boolean redirectAfterValidation) {
        this.redirectAfterValidation = redirectAfterValidation;
    }

    public final void setExceptionOnValidationFailure(final boolean exceptionOnValidationFailure) {
        this.exceptionOnValidationFailure = exceptionOnValidationFailure;
    }

    public final void setUseSession(final boolean useSession) {
        this.useSession = useSession;
    }


}
