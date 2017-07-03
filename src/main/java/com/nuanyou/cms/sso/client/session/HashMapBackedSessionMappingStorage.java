package com.nuanyou.cms.sso.client.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * 维护session和ticket之间的关系
 */
public final class HashMapBackedSessionMappingStorage implements SessionMappingStorage {

    private final Map<String,HttpSession> MANAGED_SESSIONS = new HashMap<String,HttpSession>();
    private final Map<String,String> ID_TO_SESSION_KEY_MAPPING = new HashMap<String,String>();
    private final Log log = LogFactory.getLog(getClass());

	public synchronized void addSessionById(String mappingId, HttpSession session) {
        ID_TO_SESSION_KEY_MAPPING.put(session.getId(), mappingId);
        MANAGED_SESSIONS.put(mappingId, session);
    	if (log.isDebugEnabled()) {
			Set<String> keys=ID_TO_SESSION_KEY_MAPPING.keySet();
			log.debug("\n********************add token to session***************************************");
			for (String key : keys) {
				log.debug("key:sessionId "+key+",value ST: "+ID_TO_SESSION_KEY_MAPPING.get(key));
			}
	    	if (session!=null) {
	    		log.debug("Session Create Time: " + new Date(session.getCreationTime()).toString());
	    		Enumeration<String> enumeration = session.getAttributeNames();
	    		while(enumeration.hasMoreElements()){
	    			String key = enumeration.nextElement();
	    			log.debug("session " + key + " = " + session.getAttribute(key));
	    		}
			}
		}
	}                               

	public synchronized void removeBySessionById(String sessionId) {
        if (log.isDebugEnabled()) {
            log.debug("Attempting to remove Session=[" + sessionId + "]");
        }
        final String key = ID_TO_SESSION_KEY_MAPPING.get(sessionId);
        if (log.isDebugEnabled()) {
            if (key != null) {
                log.debug("Found mapping for session.  Session Removed.");
            } else {
                log.debug("No mapping for session found.  Ignoring.");
            }
        }
        MANAGED_SESSIONS.remove(key);
        ID_TO_SESSION_KEY_MAPPING.remove(sessionId);
	}


	public synchronized HttpSession removeSessionByMappingId(String mappingId) {
		if (log.isDebugEnabled()) {
			log.debug("移除前所有的st和sessionid的映射是");
			Set<String> keys=ID_TO_SESSION_KEY_MAPPING.keySet();
			for (String key : keys) {
				//System.out.println("key: "+key+",value: "+ID_TO_SESSION_KEY_MAPPING.get(key));
			}
		}
		final HttpSession session = MANAGED_SESSIONS.get(mappingId);
        if (session != null) {
        	removeBySessionById(session.getId());
        }
        return session;
	}
}
