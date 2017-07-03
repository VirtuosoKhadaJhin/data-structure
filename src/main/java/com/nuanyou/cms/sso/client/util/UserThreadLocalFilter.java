

package com.nuanyou.cms.sso.client.util;

import com.nuanyou.cms.sso.client.validation.User;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Component
public final class UserThreadLocalFilter implements Filter {

    public void init(final FilterConfig filterConfig) throws ServletException {
        System.out.println("initFilter"+this.getClass().getName());
    }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpSession session = request.getSession(false);
        final User user = (User) (session == null ? request.getAttribute(AbstractFilter.SSO_USER) : session.getAttribute(AbstractFilter.SSO_USER));

        try {
            UserHolder.setUser(user);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserHolder.clear();
        }
    }

    public void destroy() {
        // nothing to do
    }
}
