package com.nuanyou.cms.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Felix on 2016/10/28.
 */
public class SystemContext {

    private static ThreadLocal<HttpServletResponse> _response = new ThreadLocal<HttpServletResponse>();
    private static ThreadLocal<HttpSession> _session = new ThreadLocal<HttpSession>();
    private static ThreadLocal<HttpServletRequest> request_ = new ThreadLocal<HttpServletRequest>();

    public static HttpServletResponse get_response() {
        HttpServletResponse response = (HttpServletResponse) _response.get();
        return response;
    }

    public static HttpSession get_session() {
        HttpSession session = (HttpSession) _session.get();
        return session;
    }

    public static HttpServletRequest get_request() {
        HttpServletRequest _request = (HttpServletRequest) request_.get();
        return _request;
    }

    public static void set_session(HttpSession session) {
        _session.set(session);
    }

    public static void set_response(HttpServletResponse response) {
        _response.set(response);
    }

    public static void set_request(HttpServletRequest request) {
        request_.set(request);
    }

    public static void removeSession() {
        _session = null;
    }

    public static void removeRequest() {
        request_ = null;
    }

    public static void removeResponse() {
        _response = null;
    }



}
