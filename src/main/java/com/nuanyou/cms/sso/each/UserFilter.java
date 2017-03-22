/*
 * Copyright 2012 Hongdian, Inc. All rights reserved.
 */

package com.nuanyou.cms.sso.each;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * 
 * user zhaozhengfei
 * date 2014年2月20日
 */
public class UserFilter extends OncePerRequestFilter {



	@Override
	protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpSession session=request.getSession();
		if (session.getAttribute("user")==null) {
			String username=request.getRemoteUser();
//			ServletContext sc=request.getSession().getServletContext();
//			ApplicationContext ctx= WebApplicationContextUtils.getWebApplicationContext(sc);
//			OaService oaService=(OaService)ctx.getBean(OaServiceClient.class);
//			Oauser userInfo=oaService.getUserByName(username);
//			request.getSession().setAttribute("user",userInfo);
		}
//	    SystemContext.set_request((HttpServletRequest) request);
//	    SystemContext.set_session(((HttpServletRequest) request).getSession());
//	    SystemContext.set_response(response);
		filterChain.doFilter(request, response);
	}






}
