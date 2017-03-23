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
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Component
public final class HttpServletRequestWrapperFilter extends AbstractConfigurationFilter {

    /** Name of the attribute used to answer role membership queries */
    private String roleAttribute;
   
    /** Whether or not to ignore case in role membership queries */
    private boolean ignoreCase;

    public void destroy() {
        // nothing to do
    }

    /**
     * Wraps the HttpServletRequest in a wrapper class that delegates
     * <code>request.getRemoteUser</code> to the underlying Assertion object
     * stored in the user session.
     */
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final User user = retrievePrincipalFromSessionOrRequest(servletRequest);

        filterChain.doFilter(new CasHttpServletRequestWrapper((HttpServletRequest) servletRequest, user), servletResponse);
    }

    protected User retrievePrincipalFromSessionOrRequest(final ServletRequest servletRequest) {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpSession session = request.getSession(false);
        final User user = (User) (session == null ?
                request.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION) :
                session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION));
        return user;
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
        System.out.println("initFilter"+this.getClass().getName());
        this.roleAttribute = getPropertyFromInitParams(filterConfig, "roleAttribute", null);
        this.ignoreCase = Boolean.parseBoolean(getPropertyFromInitParams(filterConfig, "ignoreCase", "false"));
    }

    final class CasHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final User user;

        CasHttpServletRequestWrapper(final HttpServletRequest request, final User user) {
            super(request);
            this.user = user;
        }

        public User getUser() {
            return this.user;
        }

        public String getRemoteUser() {
            return user != null ? this.user.getName() : null;
        }

        public boolean isUserInRole(final String role) {
            if (CommonUtils.isBlank(role)) {
                log.debug("No valid role provided.  Returning false.");
                return false;
            }

            if (this.user == null) {
                log.debug("No Principal in Request.  Returning false.");
                return false;
            }

            if (CommonUtils.isBlank(roleAttribute)) {
                log.debug("No Role Attribute Configured. Returning false.");
                return false;
            }
//
//            final Object value = this.user.getAttributes().get(roleAttribute);
//
//            if (value instanceof Collection<?>) {
//                for (final Object o : (Collection<?>) value) {
//                    if (rolesEqual(role, o)) {
//                        log.debug("User [" + getRemoteUser() + "] is in role [" + role + "]: " + true);
//                        return true;
//                    }
//                }
//            }
//
//            final boolean isMember = rolesEqual(role, value);
//            log.debug("User [" + getRemoteUser() + "] is in role [" + role + "]: " + isMember);
//            return isMember;
            return true;
        }
        
        /**
         * Determines whether the given role is equal to the candidate
         * role attribute taking into account case sensitivity.
         *
         * @param given  Role under consideration.
         * @param candidate Role that the current user possesses.
         *
         * @return True if roles are equal, false otherwise.
         */
        private boolean rolesEqual(final String given, final Object candidate) {
            return ignoreCase ? given.equalsIgnoreCase(candidate.toString()) : given.equals(candidate);
        }
    }
}
