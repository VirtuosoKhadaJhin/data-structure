package com.nuanyou.cms.sso.client.session;

import com.nuanyou.cms.sso.client.session.service.SignInOrSignOutHandler;
import com.nuanyou.cms.sso.client.util.AbstractConfigurationFilter;
import com.nuanyou.cms.sso.client.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 注册session和移除session的Filter
 *
 * @author Felix
 */
@Component
public final class SignInOrSignOutFilter extends AbstractConfigurationFilter {

    @Autowired
    private SignInOrSignOutHandler handler;

    private Pattern urlExcludePattern;

    public void init(final FilterConfig filterConfig) throws ServletException {
        System.out.println("initFilter" + this.getClass().getName());
    }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (CommonUtils.isRequestExcluded(request, urlExcludePattern)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (handler.isTokenRequest(request)) {
            log.debug("This is a link with token!");
            handler.recordSession(request);
        } else if (handler.isLogoutRequest(request)) {
            log.debug("This is a link with logout!");
            handler.destroySession(request);
            return;
        } else {
            log.debug("This is a link with no any signs,maybe it has a new Link or a link with user");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
    }


    public final void setUrlExcludePattern(Pattern urlExcludePattern) {
        this.urlExcludePattern = urlExcludePattern;
    }

    public SignInOrSignOutFilter() {
        System.out.println("instantiate the SingleSignOutFilter");
    }
}
