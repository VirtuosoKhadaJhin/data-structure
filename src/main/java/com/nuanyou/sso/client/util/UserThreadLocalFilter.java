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

package com.nuanyou.sso.client.util;

import com.nuanyou.sso.client.validation.User;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Places the assertion in a ThreadLocal such that other resources can access it that do not have access to the web tier session.
 *
 * @author Scott Battaglia
 * @version $Revision: 11728 $ $Date: 2007-09-26 14:20:43 -0400 (Tue, 26 Sep 2007) $
 * @since 3.0
 */
@Component
public final class UserThreadLocalFilter implements Filter {

    public void init(final FilterConfig filterConfig) throws ServletException {
        System.out.println("initFilter"+this.getClass().getName());
        // nothing to do here
    }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpSession session = request.getSession(false);
        final User user = (User) (session == null ? request.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION) : session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION));

        try {
            UserHolder.setUser(user);
            System.out.println("UserHolder.getUser()"+UserHolder.getUser());
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserHolder.clear();
        }
    }

    public void destroy() {
        // nothing to do
    }
}
