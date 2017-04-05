package com.nuanyou.cms.sso.client.authentication;

import com.nuanyou.cms.sso.client.ticket.ExpirationPolicy;
import com.nuanyou.cms.sso.client.ticket.GrantTicketService;
import com.nuanyou.cms.sso.client.ticket.TicketRegistry;
import com.nuanyou.cms.sso.client.util.AbstractCasFilter;
import com.nuanyou.cms.sso.client.util.CommonUtils;
import com.nuanyou.cms.sso.client.util.RandomUtils;
import com.nuanyou.cms.sso.client.validation.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Pattern;


@Component
public class AuthenticationFilter extends AbstractCasFilter {
    /**
     * The URL to the ssp Server login.
     */
    private String loginUrl;


    /**
     * Whether to send the renew request or not.
     */
    private boolean renew = false;


    private Pattern urlExcludePattern;

    private String state;
    /**
     * Whether the request include a renew or not.
     */
    private Boolean relogin;

    @Autowired
    private TicketRegistry ticketRegistry;
    @Autowired
    private ExpirationPolicy expirationPolicy;
    @Autowired
    private GrantTicketService grantStateTicket;


    protected void initInternal(final FilterConfig filterConfig) throws ServletException {
        if (!isIgnoreInitConfiguration()) {
            super.initInternal(filterConfig);
            setLoginUrl(getPropertyFromInitParams(filterConfig, "loginUrl", null));
            log.trace("Loaded loginUrl parameter: " + this.loginUrl);
            setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "false")));
            log.trace("Loaded renew parameter: " + this.renew);

            final String gatewayStorageClass = getPropertyFromInitParams(filterConfig, "gatewayStorageClass", null);


        }

    }

    public void init() {
        System.out.println("initFilter"+this.getClass().getName());
        super.init();
        CommonUtils.assertNotNull(this.loginUrl, "loginUrl cannot be null.");
    }


    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("dofilter of authenticationfilter" + request.getRequestURL() +"?"+ request.getQueryString());
        final HttpSession session = request.getSession(false);
        if (CommonUtils.isRequestExcluded(request, urlExcludePattern)) {
            filterChain.doFilter(request, response);
            return;
        }
        final User
                user = session != null ? (User) session.getAttribute(SSO_USER) : null;
        if (user != null) {
            log.info("user" + user.toString());
        } else {
            log.info("First Step:no user found");
        }
        if (user != null) {
            filterChain.doFilter(request, response);
            return;
        }
        final String serviceUrl = constructServiceUrl(request, response);
        final String ticket = CommonUtils.safeGetParameter(request, getArtifactParameterName());

        if (CommonUtils.isNotBlank(ticket)) {
            log.info("Second Step:ticket found and begin to valicate code");
            filterChain.doFilter(request, response);
            return;
        }else{
            log.info("Second Step:not ticket");
        }
        final String modifiedServiceUrl;

        modifiedServiceUrl = serviceUrl;
        log.info("First Step:Constructed service url: " + modifiedServiceUrl);
        String state= RandomUtils.randomNumber(8);
        while (ticketRegistry.getTicket(state)!=null){
            state= RandomUtils.randomNumber(8);
        }
        setState(state);
        //StateTicket stateTicket=grantStateTicket.grantStateTicket(this.state,expirationPolicy,modifiedServiceUrl);
        //this.ticketRegistry.addTicket(stateTicket);
        final String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.loginUrl, getServiceParameterName(), modifiedServiceUrl,this.state,this.relogin, this.renew);
        log.info("First Step:redirecting to \"" + urlToRedirectTo + "\"");
        response.sendRedirect(urlToRedirectTo);
    }





    public final void setRenew(final boolean renew) {
        this.renew = renew;
    }


    public final void setLoginUrl(final String loginUrl) {
        this.loginUrl = loginUrl;
    }



    public final void setUrlExcludePattern(Pattern urlExcludePattern) {
        this.urlExcludePattern = urlExcludePattern;
    }


    public void setState(String state) {
        this.state = state;
    }


    public Boolean getRelogin() {
        return relogin;
    }

    public void setRelogin(Boolean relogin) {
        this.relogin = relogin;
    }

    public AuthenticationFilter() {
    }




}
