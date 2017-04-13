

package com.nuanyou.cms.sso.client.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Listener to detect when an HTTP session is destroyed and remove it from the map of
 * managed sessions.  Also allows for the programmatic removal of sessions.
 *
 */
@WebListener
public final class SingleSignOutHttpSessionListener implements HttpSessionListener {
	protected final Log log = LogFactory.getLog(getClass());

	private SessionMappingStorage sessionMappingStorage;
	
    public void sessionCreated(final HttpSessionEvent event) {
        System.out.println("HTTP session is successfully created at the moment");
        // nothing to do at the moment
    }

    public void sessionDestroyed(final HttpSessionEvent event) {
        System.out.println("HTTP session is destroyed and remove it from the map of managed sessions");
    	if (sessionMappingStorage == null) {
    	    sessionMappingStorage = getSessionMappingStorage();
    	}
        final HttpSession session = event.getSession();
        sessionMappingStorage.removeBySessionById(session.getId());
    }

    /**
     * Obtains a {@link SessionMappingStorage} object. Assumes this method will always return the same
     * instance of the object.  It assumes this because it generally lazily calls the method.
     * 
     * @return the SessionMappingStorage
     */
    protected static SessionMappingStorage getSessionMappingStorage() {
    	return SingleSignOutFilter.getSingleSignOutHandler().getSessionMappingStorage();
    }
}
