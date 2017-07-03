package com.nuanyou.cms.sso.client.authentication;

import com.nuanyou.cms.sso.client.util.AbstractFilter;
import com.nuanyou.cms.sso.client.util.CommonUtils;
import com.nuanyou.cms.sso.client.validation.TicketStateService;
import com.nuanyou.cms.sso.client.validation.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Pattern;


@Component
public class AuthenticationFilter extends AbstractFilter {


    private String loginUrl;
    private Pattern urlExcludePattern;
    private String state;
    private Boolean relogin;
    @Autowired
    private TicketStateService ticketStateService;



    public final void init(final FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);//验证service和ticket
        setLoginUrl(getPropertyFromInitParams(filterConfig, "loginUrl", null));
        System.out.println("initFilter" + this.getClass().getName());
        CommonUtils.assertNotNull(this.loginUrl, "loginUrl cannot be null.");
    }


    /**
     *  1 排除拦截
     *  2 如果已经是登录用户继续向下一个filter
     *  2 如果有ticket，则跑到下一个filter去验证ticket
     *  3 如果都不是那么就是一个新的请求http://ssoServer:port?ret=dfgdfg
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final HttpSession session = request.getSession(false);
        if (CommonUtils.isRequestExcluded(request, urlExcludePattern)) {
            filterChain.doFilter(request, response);
            return;
        }
        final User user = session != null ? (User) session.getAttribute(SSO_USER) : null;
        if (user != null) {
            log.debug("user" + user.toString());
        } else {
            log.debug("First Step:no user found");
        }
        if (user != null) {
            filterChain.doFilter(request, response);
            return;
        }
        final String serviceUrl = constructServiceUrl(request, response);
        final String ticket = CommonUtils.safeGetParameter(request, getArtifactParameterName());

        if (CommonUtils.isNotBlank(ticket)) {//有ticket说明客户端已经拿到了ticket 直接去验证
            log.debug("Second Step:ticket found and begin to validate code");
            filterChain.doFilter(request, response);
            return;
        } else {
            log.debug("Second Step:not ticket");
        }
        log.debug("First Step:Constructed service url: " + serviceUrl);
//        StateTicket stateTicket=new StateTicket( RandomUtils.randomNumber(8),new Date());
//        while (ticketStateService.getTicket(state)!=null){
//            stateTicket=new StateTicket( RandomUtils.randomNumber(8),new Date());
//        }
//        ticketStateService.addTicket(stateTicket);
        String urlRelogin= CommonUtils.safeGetParameter(request, "relogin");
        if(CommonUtils.isNotBlank(urlRelogin)){
            this.relogin=new Boolean(urlRelogin);
        }
//        String state=stateTicket.getCode();
        String state=null;
        final String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.loginUrl, getServiceParameterName(), serviceUrl, state, this.relogin);
        log.debug("First Step:redirecting to \"" + urlToRedirectTo + "\"");
        response.sendRedirect(urlToRedirectTo);
    }


    public final void setLoginUrl(final String loginUrl) {
        this.loginUrl = loginUrl;
    }


    public final void setUrlExcludePattern(Pattern urlExcludePattern) {
        this.urlExcludePattern = urlExcludePattern;
    }


    public void setState(String state) {
        this.state = state;
    }


    public void setRelogin(Boolean relogin) {
        this.relogin = relogin;
    }

    public AuthenticationFilter() {
    }

    public static void main1(String[] args) {
        String urlExcludePattern="/test|^/dist/.|^/favicon.*";
        String url="/dist/55";
        Pattern compile = Pattern.compile(urlExcludePattern);
        Boolean excluded=compile != null
            && compile.matcher(url).matches();
        System.out.println(excluded);
    }


}
