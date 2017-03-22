package com.nuanyou.cms.sso.client.authentication;

import com.nuanyou.cms.sso.client.ticket.ExpirationPolicy;
import com.nuanyou.cms.sso.client.ticket.GrantTicketService;
import com.nuanyou.cms.sso.client.ticket.StateTicket;
import com.nuanyou.cms.sso.client.ticket.TicketRegistry;
import com.nuanyou.cms.sso.client.util.AbstractCasFilter;
import com.nuanyou.cms.sso.client.util.CommonUtils;
import com.nuanyou.cms.sso.client.validation.User;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Pattern;

@Component
public class AuthenticationFilter extends AbstractCasFilter {
    /**
     * The URL to the CAS Server login.
     */
    private String loginUrl;


    /**
     * Whether to send the renew request or not.
     */
    private boolean renew = false;

    /**
     * Whether to send the gateway request or not.
     */
    private boolean gateway = false;

    private GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();

    private Pattern urlExcludePattern;

    private String state;

    private Boolean relogin;


    @Autowired
    private TicketRegistry ticketRegistry;
    @Autowired
    private ExpirationPolicy expirationPolicy;


    protected void initInternal(final FilterConfig filterConfig) throws ServletException {
        if (!isIgnoreInitConfiguration()) {
            super.initInternal(filterConfig);
            setLoginUrl(getPropertyFromInitParams(filterConfig, "loginUrl", null));
            log.trace("Loaded CasServerLoginUrl parameter: " + this.loginUrl);
            setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "false")));
            log.trace("Loaded renew parameter: " + this.renew);
            setGateway(parseBoolean(getPropertyFromInitParams(filterConfig, "gateway", "false")));
            log.trace("Loaded gateway parameter: " + this.gateway);

            final String gatewayStorageClass = getPropertyFromInitParams(filterConfig, "gatewayStorageClass", null);

            if (gatewayStorageClass != null) {
                try {
                    this.gatewayStorage = (GatewayResolver) Class.forName(gatewayStorageClass).newInstance();
                } catch (final Exception e) {
                    log.error(e, e);
                    throw new ServletException(e);
                }
            }
        }

        //exclude url
//        String urlExcludeStr = filterConfig.getInitParameter("urlExcludePattern");
//        if (urlExcludeStr != null) {
//            // lance une PatternSyntaxException si la syntaxe du pattern est invalide
//            urlExcludePattern = Pattern.compile(urlExcludeStr);
//        }

    }

    public void init() {
        super.init();


        CommonUtils.assertNotNull(this.loginUrl, "casServerLoginUrl cannot be null.");
    }


    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("dofilter of authenticationfilter" + request.getRequestURL() +""+ request.getQueryString());
        final HttpSession session = request.getSession(false);
        if (CommonUtils.isRequestExcluded(request, urlExcludePattern)) {
            filterChain.doFilter(request, response);
            return;
        }
        final User
                user = session != null ? (User) session.getAttribute(CONST_CAS_ASSERTION) : null;
        if (user != null) {
            log.debug("user" + user.toString());
        } else {
            log.debug("user is null");
        }
        if (user != null) {
            filterChain.doFilter(request, response);
            return;
        }
        final String serviceUrl = constructServiceUrl(request, response);
        final String ticket = CommonUtils.safeGetParameter(request, getArtifactParameterName());
        final boolean wasGatewayed = this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);

        if (CommonUtils.isNotBlank(ticket) || wasGatewayed) {
            filterChain.doFilter(request, response);
            return;
        }
        final String modifiedServiceUrl;
        log.debug("no ticket and no assertion found");
        if (this.gateway) {
            log.debug("setting gateway attribute in n");
            modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);
        } else {
            modifiedServiceUrl = serviceUrl;
        }

        if (log.isDebugEnabled()) {
            log.debug("Constructed service url: " + modifiedServiceUrl);
        }
        this.setState(RandomUtils.nextBytes(10000000).toString());
        StateTicket stateTicket=grantStateTicket.grantStateTicket(this.state,expirationPolicy,modifiedServiceUrl);
        //放入缓存
        this.ticketRegistry.addTicket(stateTicket);
        final String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.loginUrl, getServiceParameterName(), modifiedServiceUrl,this.state,this.relogin, this.renew, this.gateway);

        if (log.isDebugEnabled()) {
            log.debug("redirecting to \"" + urlToRedirectTo + "\"");
            log.debug("由于没有登录 所以重定向到服务器");
        }
        response.sendRedirect(urlToRedirectTo);
    }

@Autowired private GrantTicketService grantStateTicket;


    public final void setRenew(final boolean renew) {
        this.renew = renew;
    }

    public final void setGateway(final boolean gateway) {
        this.gateway = gateway;
    }

    public final void setLoginUrl(final String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public final void setGatewayStorage(final GatewayResolver gatewayStorage) {
        this.gatewayStorage = gatewayStorage;
    }


    public final void setUrlExcludePattern(Pattern urlExcludePattern) {
        this.urlExcludePattern = urlExcludePattern;
    }


    public void setState(String state) {
        this.state = state;
    }


    public Boolean getRelogin() {
        return relogin;
    }

    public void setRelogin(Boolean relogin) {
        this.relogin = relogin;
    }

    public AuthenticationFilter() {
    }

//    public static void main(String[] args) {
//        Pattern urlExcludePattern = Pattern.compile("/test|^/dist.*|^/favicon.*");
//        System.out.println(urlExcludePattern.matcher("/favicon.ico").matches());
//    }


//    public static void main(String[] args) {
//        System.out.println(Math.PI+"");
//    }

}
