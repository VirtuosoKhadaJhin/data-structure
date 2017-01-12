package com.nuanyou.cms.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Felix on 2016/10/28.
 */
public class SystemContext {

    private static ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();



    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) requestThreadLocal.get();
    }



    protected static void setRequest(HttpServletRequest request) {
        requestThreadLocal.set(request);
    }


    protected static void removeRequest() {
        requestThreadLocal.remove();
    }







}
