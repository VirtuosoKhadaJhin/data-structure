package com.nuanyou.cms.sso.client.authentication;

import com.nuanyou.cms.sso.client.util.AbstractFilter;
import com.nuanyou.cms.sso.client.util.CommonUtils;
import com.nuanyou.cms.sso.client.util.RandomUtils;
import com.nuanyou.cms.sso.client.validation.User;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Pattern;


@Component
public class AuthenticationFilter extends AbstractFilter {


    /**
     * sso服务器登录的根
     */
    private String loginUrl;


    /**
     * The URL to the sso Server login.
     */
    private Pattern urlExcludePattern;

    private String state;

    /**
     * 是否需要强制登录
     */
    private Boolean relogin;



    protected void initInternal(final FilterConfig filterConfig) throws ServletException {
        if (!isIgnoreInitConfiguration()) {
            super.initInternal(filterConfig);
            setLoginUrl(getPropertyFromInitParams(filterConfig, "loginUrl", null));
            log.trace("Loaded loginUrl parameter: " + this.loginUrl);

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
        final HttpSession session = request.getSession(false);
        if (CommonUtils.isRequestExcluded(request, urlExcludePattern)) {
            filterChain.doFilter(request, response);
            return;
        }
        final User
                user = session != null ? (User) session.getAttribute(SSO_USER) : null;
        if (user != null) {
            log.debug("user" + user.toString());
        } else {
            log.debug("First Step:no user found");
        }
        if (user != null) {
            filterChain.doFilter(request, response);
            return;
        }
        final String serviceUrl = constructServiceUrl(request, response);
        final String ticket = CommonUtils.safeGetParameter(request, getArtifactParameterName());

        if (CommonUtils.isNotBlank(ticket)) {
            log.debug("Second Step:ticket found and begin to valicate code");
            filterChain.doFilter(request, response);
            return;
        }else{
            log.debug("Second Step:not ticket");
        }
        final String modifiedServiceUrl;

        modifiedServiceUrl = serviceUrl;
        log.debug("First Step:Constructed service url: " + modifiedServiceUrl);
        String state= RandomUtils.randomNumber(8);
//        while (ticketRegistry.getTicket(state)!=null){
//            state= RandomUtils.randomNumber(8);
//        }
        setState(state);
        //StateTicket stateTicket=grantStateTicket.grantStateTicket(this.state,expirationPolicy,modifiedServiceUrl);
        //this.ticketRegistry.addTicket(stateTicket);
        final String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.loginUrl, getServiceParameterName(), modifiedServiceUrl,this.state,this.relogin);
        log.debug("First Step:redirecting to \"" + urlToRedirectTo + "\"");
        response.sendRedirect(urlToRedirectTo);
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
