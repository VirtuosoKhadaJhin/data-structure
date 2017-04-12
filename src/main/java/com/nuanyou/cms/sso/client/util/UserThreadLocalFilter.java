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

package com.nuanyou.cms.sso.client.util;

import com.nuanyou.cms.sso.client.validation.User;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Places the User in a ThreadLocal such that other resources can access it that do not have access to the web tier session.
 */
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
