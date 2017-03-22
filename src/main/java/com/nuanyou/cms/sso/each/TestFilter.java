package com.nuanyou.cms.sso.each;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Felix on 2016/9/19.
 */
@WebFilter(filterName = "myFilter", urlPatterns = "/test")
public class TestFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("TestFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        out.write("TestFilter2017-1-51144");
        out.close();
        return;
    }

    @Override
    public void destroy() {
        System.out.println("init destroy");
    }
}
