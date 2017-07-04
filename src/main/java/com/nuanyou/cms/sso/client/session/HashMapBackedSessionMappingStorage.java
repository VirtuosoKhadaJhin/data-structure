package com.nuanyou.cms.sso.client.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * 维护session和ticket之间的关系
 */
@Service
public final class HashMapBackedSessionMappingStorage implements SessionMappingStorage {

    private final Log log = LogFactory.getLog(getClass());
    private final Map<String, HttpSession> MANAGED_SESSIONS = new HashMap<String, HttpSession>();
    private final Map<String, String> ID_TO_SESSION_KEY_MAPPING = new HashMap<String, String>();

    public synchronized void addSessionById(String mappingId, HttpSession session) {
        ID_TO_SESSION_KEY_MAPPING.put(session.getId(), mappingId);
        MANAGED_SESSIONS.put(mappingId, session);
    }

    public synchronized void removeBySessionById(String sessionId) {
        final String key = ID_TO_SESSION_KEY_MAPPING.get(sessionId);
        MANAGED_SESSIONS.remove(key);
        ID_TO_SESSION_KEY_MAPPING.remove(sessionId);
    }


    public synchronized HttpSession removeSessionByMappingId(String mappingId) {
        if (log.isDebugEnabled()) {
            log.debug("移除前所有的st和sessionid的映射是");
            Set<String> keys = ID_TO_SESSION_KEY_MAPPING.keySet();
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
