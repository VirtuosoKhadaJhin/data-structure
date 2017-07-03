package com.nuanyou.cms.sso.client.session;

import com.nuanyou.cms.sso.client.util.AbstractConfigurationFilter;
import com.nuanyou.cms.sso.client.util.CommonUtils;
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
public final class SingleSignOutFilter extends AbstractConfigurationFilter {

    private static final SingleSignOutHandler handler = new SingleSignOutHandler();
    private Pattern urlExcludePattern;

    public void init(final FilterConfig filterConfig) throws ServletException {
        //初始化时得到ticket的name
        System.out.println("initFilter" + this.getClass().getName());
        handler.setArtifactParameterName(getPropertyFromInitParams(filterConfig, "artifactParameterName", "code"));
        //handler.setLogoutParameterName(getPropertyFromInitParams(filterConfig, "logoutParameterName", "logoutRequest"));
        handler.init();
    }


    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
//		log.info("SingleSignOutFilter"+request.getRequestURL()+"?"+request.getQueryString());
        if (CommonUtils.isRequestExcluded(request, urlExcludePattern)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (handler.isTokenRequest(request)) {
            log.debug("这是一个有ticket的链接!");
            handler.recordSession(request);
        } else if (handler.isLogoutRequest(request)) {
            log.debug("这是一个注销的链接!");
            handler.destroySession(request);
            return;
        } else {
            log.debug("这是一个其他的链接!");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
    }

    protected static SingleSignOutHandler getSingleSignOutHandler() {
        return handler;
    }

    public final void setUrlExcludePattern(Pattern urlExcludePattern) {
        this.urlExcludePattern = urlExcludePattern;
    }

    public SingleSignOutFilter() {
        System.out.println("instantiate the SingleSignOutFilter");
    }
}
