
package com.nuanyou.cms.sso.client.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
            return value;
        }
        return defaultValue;
    }

}
