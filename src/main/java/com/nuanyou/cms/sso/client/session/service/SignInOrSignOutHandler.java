package com.nuanyou.cms.sso.client.session.service;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * 存储session和ticket之间的映射
 * 
 * @author Felix
 */
public interface SignInOrSignOutHandler {

	public boolean isTokenRequest(final HttpServletRequest request);

	public boolean isLogoutRequest(final HttpServletRequest request);

	public void recordSession(final HttpServletRequest request);

	public void destroySession(final HttpServletRequest request);

}
