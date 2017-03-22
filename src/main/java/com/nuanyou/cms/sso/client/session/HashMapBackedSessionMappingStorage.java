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

package com.nuanyou.cms.sso.client.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * HashMap backed implementation of SessionMappingStorage.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 *
 */
public final class HashMapBackedSessionMappingStorage implements SessionMappingStorage {
	
    /**
     * Maps the ID from the CAS server to the Session.
     */
    private final Map<String,HttpSession> MANAGED_SESSIONS = new HashMap<String,HttpSession>();

    /**
     * Maps the Session ID to the key from the CAS Server.
     */
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
