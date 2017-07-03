package com.nuanyou.cms.sso.client.session;

import com.nuanyou.cms.sso.client.util.CommonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 登入登出的操作
 *
 * @author Felix
 *
 */
public final class SingleSignOutHandler {

    private final Log log = LogFactory.getLog(getClass());
    private SessionMappingStorage sessionMappingStorage = new HashMapBackedSessionMappingStorage();
    private String artifactParameterName = "ticket";
    private String logoutParameterName = "logoutRequest";


    public void setSessionMappingStorage(final SessionMappingStorage storage) {
        this.sessionMappingStorage = storage;
    }

    public SessionMappingStorage getSessionMappingStorage() {
        return this.sessionMappingStorage;
    }

    public void setArtifactParameterName(final String name) {
        this.artifactParameterName = name;
    }

    public void setLogoutParameterName(final String name) {
        this.logoutParameterName = name;
    }


    public void init() {
        CommonUtils.assertNotNull(this.artifactParameterName, "artifactParameterName cannot be null.");
        CommonUtils.assertNotNull(this.logoutParameterName, "logoutParameterName cannot be null.");
        CommonUtils.assertNotNull(this.sessionMappingStorage, "sessionMappingStorage cannote be null.");
    }


    /**
     * request是否含有登录验证ticket
     * @param request
     * @return
     */
    public boolean isTokenRequest(final HttpServletRequest request) {
        return CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.artifactParameterName));
    }

    /**
     * 标识给的请求是否是sso服务器传来的注销请求
     *
     */
    public boolean isLogoutRequest(final HttpServletRequest request) {
        return "GET".equals(request.getMethod()) && !isMultipartRequest(request) &&
                CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.logoutParameterName));
    }

    /**
     * 根据ticket注册一个session
     */
    public void recordSession(final HttpServletRequest request) {
        final HttpSession session = request.getSession(true);
        final String token = CommonUtils.safeGetParameter(request, this.artifactParameterName);
        log.debug("Recording session for token " + token);    //根据sessionid 移除 对应的ST 和 session
        this.sessionMappingStorage.removeBySessionById(session.getId());
        sessionMappingStorage.addSessionById(token, session);
    }

    public void destroySession(final HttpServletRequest request) {
        final String logoutMessage = CommonUtils.safeGetParameter(request, this.logoutParameterName);
        if (log.isTraceEnabled()) {
            log.trace("Logout request:\n" + logoutMessage);
        }
        log.debug("logoutMessage:  " + logoutMessage);
        final String token = logoutMessage;
        if (CommonUtils.isNotBlank(token)) {
            final HttpSession session = this.sessionMappingStorage.removeSessionByMappingId(token);
            if (session != null) {
                String sessionID = session.getId();
                log.debug("Invalidating session [" + sessionID + "] for token [" + token + "]");
                try {
                    session.invalidate();
                } catch (final IllegalStateException e) {
                    log.debug("Error invalidating session.", e);
                }
            }
        }
    }

    private boolean isMultipartRequest(final HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().toLowerCase().startsWith("multipart");
    }


}
