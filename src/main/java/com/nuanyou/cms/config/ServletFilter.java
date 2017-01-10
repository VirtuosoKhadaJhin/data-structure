package com.nuanyou.cms.config;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Felix on 2016/9/19.
 */
@WebFilter(filterName = "system", urlPatterns = "/*")
public class ServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            SystemContext.setRequest((HttpServletRequest)request);
            chain.doFilter(request, response);
        } finally {
            SystemContext.removeRequest();
        }
    }

    @Override
    public void destroy() {
    }
}
