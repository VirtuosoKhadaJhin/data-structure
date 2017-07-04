

package com.nuanyou.cms.sso.client.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 监控器:为了侦测httpsession销毁,销毁时把对应的ticket映射删除
 */
@WebListener
public final class SingleSignOutHttpSessionListener implements HttpSessionListener {



	protected final Log log = LogFactory.getLog(getClass());
    @Autowired
	private SessionMappingStorage sessionMappingStorage;
	
    public void sessionCreated(final HttpSessionEvent event) {
        System.out.println("HTTP session is successfully created at the moment");
    }

    public void sessionDestroyed(final HttpSessionEvent event) {
        System.out.println("HTTP session is destroyed and remove it from the map of managed sessions");
        final HttpSession session = event.getSession();
        sessionMappingStorage.removeBySessionById(session.getId());
    }


}
