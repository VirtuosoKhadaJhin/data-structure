

package com.nuanyou.cms.sso.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.nuanyou.cms.sso.client.util.ParameterConfig.TicketParameterName;

/**
 *  抽象的公共Filter(目的是为了初始化一些公共属性)
 */
public abstract class AbstractFilter extends AbstractConfigurationFilter {

    protected static final Logger log = LoggerFactory.getLogger(AbstractFilter.class.getSimpleName());

    /*服务器地址,格式是http/https：hostname:port ,标准的端口号可以不写*/
    private String serverName;

    protected final String constructServiceUrl(final HttpServletRequest request, final HttpServletResponse response) {
        return CommonUtils.constructServiceUrl(request, response, this.serverName, TicketParameterName);
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

    /**
     * 初始化serverName,ticketName：code,serviceCallbackName：ret
     * @param filterConfig
     * @throws ServletException
     */
    public  void init(final FilterConfig filterConfig) throws ServletException {
        setServerName(getPropertyFromInitParams(filterConfig, "serverName", null));
        CommonUtils.assertTrue(CommonUtils.isNotEmpty(this.serverName), "serverName must be set.");
    }



    public void destroy() {
        // nothing to do
    }

    public final String getServerName() {
        return serverName;
    }
    
}
