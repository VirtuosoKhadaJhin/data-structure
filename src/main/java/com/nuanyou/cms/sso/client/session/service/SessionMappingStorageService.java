package com.nuanyou.cms.sso.client.session.service;

import javax.servlet.http.HttpSession;

/**
 *
 * 存储session和ticket之间的映射
 * 
 * @author Felix
 */
public interface SessionMappingStorageService {

	HttpSession removeSessionByMappingId(String mappingId);

	void removeBySessionById(String sessionId);

	void addSessionById(String mappingId, HttpSession session);

}
