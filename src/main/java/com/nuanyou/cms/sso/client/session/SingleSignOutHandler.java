package com.nuanyou.cms.sso.client.session;

import com.nuanyou.cms.sso.client.util.CommonUtils;
import com.nuanyou.cms.sso.client.util.ParameterConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 登入登出的操作
 *
 * @author Felix
 *
 */
@Service
public final class SingleSignOutHandler {

    private final Log log = LogFactory.getLog(getClass());
    @Autowired
    private SessionMappingStorage sessionMappingStorage;
    private final String ticketParameterName = ParameterConfig.ticketParameterName;
    private final String logoutParameterName = ParameterConfig.logoutParameterName;



    public void init() {
    }


    /**
     * request是否含有登录验证ticket
     * @param request
     * @return
     */
    public boolean isTokenRequest(final HttpServletRequest request) {
        return CommonUtils.isNotBlank(request.getParameter(this.ticketParameterName));
    }

    /**
     * 标识给的请求是否是sso服务器传来的注销请求
     *
     */
    public boolean isLogoutRequest(final HttpServletRequest request) {
        return "GET".equals(request.getMethod())  &&
                CommonUtils.isNotBlank(request.getParameter(this.logoutParameterName));
    }

    /**
     * 根据ticket注册一个session
     */
    public void recordSession(final HttpServletRequest request) {
        final HttpSession session = request.getSession(true);
        final String token = CommonUtils.safeGetParameter(request, this.ticketParameterName);
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





}
