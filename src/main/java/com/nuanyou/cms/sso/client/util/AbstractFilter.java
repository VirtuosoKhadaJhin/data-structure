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

package com.nuanyou.cms.sso.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  抽象的公共Filter(目的是为了初始化一些公共属性)
 */
public abstract class AbstractFilter extends AbstractConfigurationFilter {

    /*放在内存的用户实例标志*/
    public static final String SSO_USER = "sso_user";

    /** 记录日志. */
    protected static final Logger log = LoggerFactory.getLogger(AbstractFilter.class.getSimpleName());

    /** 定义这个参数目的是为了寻找生成的code */
    private String artifactParameterName = "code";

    /** 寻找service */
    private String serviceParameterName = "ret";

    /** 如果为true则对于serviceURL进行编号response.encodeUrl */
    private boolean encodeServiceUrl = true;

    /*服务器地址,格式是http/https：hostname:port ,标准的端口号可以不写*/
    private String serverName;



    /**
     * 初始化serverName,ticketName：code,serviceCallbackName：ret
     * @param filterConfig
     * @throws ServletException
     */
    public  void init(final FilterConfig filterConfig) throws ServletException {
        setServerName(getPropertyFromInitParams(filterConfig, "serverName", null));
        setArtifactParameterName(getPropertyFromInitParams(filterConfig, "artifactParameterName", "code"));
        setServiceParameterName(getPropertyFromInitParams(filterConfig, "serviceParameterName", "ret"));
        CommonUtils.assertNotNull(this.artifactParameterName, "artifactParameterName cannot be null.");
        CommonUtils.assertNotNull(this.serviceParameterName, "serviceParameterName cannot be null.");
        CommonUtils.assertTrue(CommonUtils.isNotEmpty(this.serverName), "serverName must be set.");
    }





    public void destroy() {
        // nothing to do
    }

    protected final String constructServiceUrl(final HttpServletRequest request, final HttpServletResponse response) {
        return CommonUtils.constructServiceUrl(request, response, this.serverName, this.artifactParameterName, this.encodeServiceUrl);
    }

    /**
     *
     *serverName不应该有斜杠
     * @param serverName
     */
    public final void setServerName(final String serverName) {
        if (serverName != null && serverName.endsWith("/")) {
            this.serverName = serverName.substring(0, serverName.length() - 1);
        } else {
            this.serverName = serverName;
        }
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
