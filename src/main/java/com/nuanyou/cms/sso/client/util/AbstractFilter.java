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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  Abstract filter that contains code that is common to all related filters.
 */
public abstract class AbstractFilter extends AbstractConfigurationFilter {

    /** Represents the constant for where the assertion will be located in memory. */
    public static final String SSO_USER = "sso_user";

    /** Instance of commons logging for logging purposes. */
    protected static final Logger log = LoggerFactory.getLogger(AbstractFilter.class.getSimpleName());

    /** Defines the parameter to look for for the artifact. */
    private String artifactParameterName = "code";

    /** Defines the parameter to look for for the service. */
    private String serviceParameterName = "ret";
    
    /** Sets where response.encodeUrl should be called on service urls when constructed. */
    private boolean encodeServiceUrl = true;

    /**
     * The name of the server.  Should be in the following format: {protocol}:{hostName}:{port}.
     * Standard ports can be excluded. */
    private String serverName;

    /** The exact url of the service. */
    private String service;

    public final void init(final FilterConfig filterConfig) throws ServletException {
        if (!isIgnoreInitConfiguration()) {
            setServerName(getPropertyFromInitParams(filterConfig, "serverName", null));
            log.trace("Loading serverName property: " + this.serverName);
            setService(getPropertyFromInitParams(filterConfig, "service", null));
            log.trace("Loading service property: " + this.service);
            setArtifactParameterName(getPropertyFromInitParams(filterConfig, "artifactParameterName", "ticket"));
            log.trace("Loading artifact parameter name property: " + this.artifactParameterName);
            setServiceParameterName(getPropertyFromInitParams(filterConfig, "serviceParameterName", "service"));
            log.trace("Loading serviceParameterName property: " + this.serviceParameterName);
            setEncodeServiceUrl(parseBoolean(getPropertyFromInitParams(filterConfig, "encodeServiceUrl", "true")));
            log.trace("Loading encodeServiceUrl property: " + this.encodeServiceUrl);

            initInternal(filterConfig);
        }
        init();
    }

    /** Controls the ordering of filter initialization and checking by defining a method that runs before the init.
     * @param filterConfig the original filter configuration.
     * @throws ServletException if there is a problem.
     *
     */
    protected void initInternal(final FilterConfig filterConfig) throws ServletException {
        // template method
    }

    /**
     * Initialization method.  Called by Filter's init method or by Spring.  Similar in concept to the InitializingBean interface's
     * afterPropertiesSet();
     */
    public void init() {
        CommonUtils.assertNotNull(this.artifactParameterName, "artifactParameterName cannot be null.");
        CommonUtils.assertNotNull(this.serviceParameterName, "serviceParameterName cannot be null.");
        CommonUtils.assertTrue(CommonUtils.isNotEmpty(this.serverName) || CommonUtils.isNotEmpty(this.service), "serverName or service must be set.");
        CommonUtils.assertTrue(CommonUtils.isBlank(this.serverName) || CommonUtils.isBlank(this.service), "serverName and service cannot both be set.  You MUST ONLY set one.");
    }

    // empty implementation as most filters won't need this.
    public void destroy() {
        // nothing to do
    }

    protected final String constructServiceUrl(final HttpServletRequest request, final HttpServletResponse response) {
        return CommonUtils.constructServiceUrl(request, response, this.service, this.serverName, this.artifactParameterName, this.encodeServiceUrl);
    }

    /**
     * Note that trailing slashes should not be used in the serverName.  As a convenience for this common misconfiguration, we strip them from the provided
     * value.
     *
     * @param serverName the serverName. If this method is called, this should not be null.  This AND service should not be both configured.
     */
    public final void setServerName(final String serverName) {
        if (serverName != null && serverName.endsWith("/")) {
            this.serverName = serverName.substring(0, serverName.length()-1);
            log.info(String.format("Eliminated extra slash from serverName [%s].  It is now [%s]", serverName, this.serverName));
        } else {
            this.serverName = serverName;
        }
    }

    public final void setService(final String service) {
        this.service = service;
    }

    public final void setArtifactParameterName(final String artifactParameterName) {
        this.artifactParameterName = artifactParameterName;
    }

    public final void setServiceParameterName(final String serviceParameterName) {
        this.serviceParameterName = serviceParameterName;
    }
    
    public final void setEncodeServiceUrl(final boolean encodeServiceUrl) {
    	this.encodeServiceUrl = encodeServiceUrl;
    }

    public final String getArtifactParameterName() {
        return this.artifactParameterName;
    }

    public final String getServiceParameterName() {
        return this.serviceParameterName;
    }
}
