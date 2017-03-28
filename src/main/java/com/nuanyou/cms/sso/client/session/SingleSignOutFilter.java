/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.nuanyou.cms.sso.client.session;
import com.nuanyou.cms.sso.client.util.AbstractConfigurationFilter;
import com.nuanyou.cms.sso.client.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Implements the Single Sign Out protocol.  It handles registering the session and destroying the session.
 *
 * @author Felix
 */
@Component
public final class SingleSignOutFilter extends AbstractConfigurationFilter {

	private static final SingleSignOutHandler handler = new SingleSignOutHandler();
	private static final Logger logger = LoggerFactory.getLogger(SingleSignOutFilter.class);
	private Pattern urlExcludePattern;

	public void init(final FilterConfig filterConfig) throws ServletException {
		System.out.println("initFilter"+this.getClass().getName());
		if (!isIgnoreInitConfiguration()) {
			handler.setArtifactParameterName(getPropertyFromInitParams(filterConfig, "artifactParameterName", "ticket"));
			handler.setLogoutParameterName(getPropertyFromInitParams(filterConfig, "logoutParameterName", "logoutRequest"));
		}
		handler.init();
	}

	public void setArtifactParameterName(final String name) {
		handler.setArtifactParameterName(name);
	}

	public void setLogoutParameterName(final String name) {
		handler.setLogoutParameterName(name);
	}

	public void setSessionMappingStorage(final SessionMappingStorage storage) {
		handler.setSessionMappingStorage(storage);
	}

	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {


		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		log.info("SingleSignOutFilter"+request.getRequestURL()+"?"+request.getQueryString());
		if(CommonUtils.isRequestExcluded(request,urlExcludePattern)){
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		if (handler.isTokenRequest(request)) {
			handler.recordSession(request);
		} else if (handler.isLogoutRequest(request)) {
			handler.destroySession(request);
			// Do not continue up filter chain
			return;
		} else {
			//log.trace("Ignoring URI " + request.getRequestURI());
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	public void destroy() {
		// nothing to do
	}

	protected static SingleSignOutHandler getSingleSignOutHandler() {
		return handler;
	}


	public SingleSignOutFilter() {
		System.out.println("instantiate the SingleSignOutFilter");
	}

	public final void setUrlExcludePattern(Pattern urlExcludePattern) {
		this.urlExcludePattern = urlExcludePattern;
	}
}
