/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.nuanyou.cms.sso.client.validation;


import com.nuanyou.cms.sso.client.util.AbstractFilter;
import com.nuanyou.cms.sso.client.util.CommonUtils;
import com.nuanyou.cms.sso.client.util.OperationLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

/**
 * The filter that handles all the work of validating ticket requests.
 *
 */
public abstract class AbstractTicketValidationFilter extends AbstractFilter {

    private static final Logger log = LoggerFactory.getLogger(AbstractTicketValidationFilter.class.getSimpleName());
    /**
     * The TicketValidator we will use to validate tickets.
     */
    private TicketValidator ticketValidator;

    /**
     * Specify whether the filter should redirect the user agent after a
     * successful validation to remove the ticket parameter from the query
     * string.
     */
    private boolean redirectAfterValidation = false;

    /**
     * Determines whether an exception is thrown when there is a ticket validation failure.
     */
    private boolean exceptionOnValidationFailure = true;

    private boolean useSession = true;

    /**
     * Template method to return the appropriate validator.
     *
     * @param filterConfig the FilterConfiguration that may be needed to construct a validator.
     * @return the ticket validator.
     */
    protected TicketValidator getTicketValidator(final FilterConfig filterConfig) {
        return this.ticketValidator;
    }



    protected void initInternal(final FilterConfig filterConfig) throws ServletException {
        setExceptionOnValidationFailure(parseBoolean(getPropertyFromInitParams(filterConfig, "exceptionOnValidationFailure", "true")));
        log.trace("Setting exceptionOnValidationFailure parameter: " + this.exceptionOnValidationFailure);
        setRedirectAfterValidation(parseBoolean(getPropertyFromInitParams(filterConfig, "redirectAfterValidation", "true")));
        log.trace("Setting redirectAfterValidation parameter: " + this.redirectAfterValidation);
        setUseSession(parseBoolean(getPropertyFromInitParams(filterConfig, "useSession", "true")));
        log.trace("Setting useSession parameter: " + this.useSession);
        setTicketValidator(getTicketValidator(filterConfig));
        super.initInternal(filterConfig);
    }

    public void init() {
        super.init();
        CommonUtils.assertNotNull(this.ticketValidator, "ticketValidator cannot be null.");
    }

    /**
     * Pre-process the request before the normal filter process starts.  This could be useful for pre-empting code.
     *
     * @param servletRequest  The servlet request.
     * @param servletResponse The servlet response.
     * @param filterChain     the filter chain.
     * @return true if processing should continue, false otherwise.
     * @throws IOException      if there is an I/O problem
     * @throws ServletException if there is a servlet problem.
     */
    protected boolean preFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        return true;
    }

    /**
     * Template method that gets executed if ticket validation succeeds.  Override if you want additional behavior to occur
     * if ticket validation succeeds.  This method is called after all ValidationFilter processing required for a successful authentication
     * occurs.
     *
     * @param request  the HttpServletRequest.
     * @param response the HttpServletResponse.
     * @param user     the successful Assertion from the server.
     */
    protected void onSuccessfulValidation(final HttpServletRequest request, final HttpServletResponse response, final User user) {
        OperationLog.log(user.getUserid(), user.getName(),request.getRequestURI(), OperationLog.Action.Login,null);
    }

    /**
     * Template method that gets executed if validation fails.  This method is called right after the exception is caught from the ticket validator
     * but before any of the processing of the exception occurs.
     *
     * @param request  the HttpServletRequest.
     * @param response the HttpServletResponse.
     */
    protected void onFailedValidation(final HttpServletRequest request, final HttpServletResponse response) {
        // nothing to do here.
    }

    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        String artifactParameterName = getArtifactParameterName();
        final String ticket = CommonUtils.safeGetParameter(request, artifactParameterName);
        final String state = CommonUtils.safeGetParameter(request, "state");
        /*
        if (CommonUtils.isNotBlank(state)) {
            log.info("Second Step:state found and validate state");
            final StateTicket stateTicket = (StateTicket) this.abstractTicketRegistry.getTicket(state, StateTicket.class);
            if(stateTicket==null){
                throw new ServletException("stateTicket不存在");
            }
            try {
                synchronized (stateTicket) {
                   if (stateTicket.isExpired()) {
                        log.info("stateTicket [" + state + "] has expired.");
                        throw new InvalidTicketException();
                    }
               }
            } catch (Exception e) {
                if (this.exceptionOnValidationFailure) {
                    throw new ServletException(e);
                }
            } finally {
                this.abstractTicketRegistry.deleteTicket(state);
            }

        }else{
            log.info("Second Step:ticket not found");
        }
        */
        if (CommonUtils.isNotBlank(ticket)) {
            log.info("Second Step:Attempting to validate ticket: " + ticket);
            try {
                final User user = this.ticketValidator.validate(ticket, constructServiceUrl(request, response));
                log.info("Second Step:Successfully authenticated user: " + user);
                request.setAttribute(SSO_USER, user);
                if (this.useSession) {
                    log.debug("\n" + "**************************************after validate tgt and st ********************************************");
                    log.debug("put the assertion to the session scope:key:" + SSO_USER + "value:" + user);
                    log.debug("assertion props" + user.toString());
                    if (request.getSession(false) != null) {
                        log.debug("Session Create Time: " + new Date(request.getSession(false).getCreationTime()).toString());
                        Cookie[] cookies = request.getCookies();
                        if (cookies == null) {
                            log.debug("cookie ==null");
                        } else {
                            for (Cookie cookie : cookies) {
                                log.debug("cookie " + cookie.getName() + " = " + cookie.getValue());
                            }
                        }
                        HttpSession session = request.getSession(false);
                        Enumeration<String> enumeration = session.getAttributeNames();
                        while (enumeration.hasMoreElements()) {
                            String key = enumeration.nextElement();
                            log.debug("session " + key + " = " + session.getAttribute(key));
                        }
                    } else {
                        log.debug("sessin is null");
                    }
                    log.debug("**************************************after validate tgt and st ********************************************" + "\n");

                    request.getSession().setAttribute(SSO_USER, user);
                }
                onSuccessfulValidation(request, response, user);

                if (this.redirectAfterValidation) {
                    log.debug("Redirecting after successful ticket validation.");
                    response.sendRedirect(constructServiceUrl(request, response));
                    return;
                }
            } catch (final TicketValidationException e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                onFailedValidation(request, response);

                if (this.exceptionOnValidationFailure) {
                    throw new ServletException(e);
                }
                return;
            }
        }

        filterChain.doFilter(request, response);

    }

    public final void setTicketValidator(final TicketValidator ticketValidator) {
        this.ticketValidator = ticketValidator;
    }

    public final void setRedirectAfterValidation(final boolean redirectAfterValidation) {
        this.redirectAfterValidation = redirectAfterValidation;
    }

    public final void setExceptionOnValidationFailure(final boolean exceptionOnValidationFailure) {
        this.exceptionOnValidationFailure = exceptionOnValidationFailure;
    }

    public final void setUseSession(final boolean useSession) {
        this.useSession = useSession;
    }


}
