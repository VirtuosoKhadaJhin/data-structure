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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;


public abstract class AbstractConfigurationFilter implements Filter {
	
	protected final Log log = LogFactory.getLog(getClass());



    /**
     * 得到Filter上下文的属性,首先检查初始化参数是否有值
     * @param filterConfig
     * @param propertyName
     * @param defaultValue
     * @return
     */
    protected final String getPropertyFromInitParams(final FilterConfig filterConfig, final String propertyName, final String defaultValue)  {
        final String value = filterConfig.getInitParameter(propertyName);
        if (CommonUtils.isNotBlank(value)) {
            log.info("Property [" + propertyName + "] loaded from FilterConfig.getInitParameter with value [" + value + "]");
            return value;
        }

        final String value2 = filterConfig.getServletContext().getInitParameter(propertyName);

        if (CommonUtils.isNotBlank(value2)) {
            log.info("Property [" + propertyName + "] loaded from ServletContext.getInitParameter with value [" + value2 + "]");
            return value2;
        }
        InitialContext context;
        try {
         context = new InitialContext();
        } catch (final NamingException e) {
        	log.warn(e,e);
        	return defaultValue;
        }
        return defaultValue;
    }
    protected final boolean parseBoolean(final String value) {
        return ((value != null) && value.equalsIgnoreCase("true"));
    }

    


}
