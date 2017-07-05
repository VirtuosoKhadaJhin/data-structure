package com.nuanyou.cms.sso.client;

import com.nuanyou.cms.sso.client.authentication.AuthenticationFilter;
import com.nuanyou.cms.sso.client.session.SignInOrSignOutFilter;
import com.nuanyou.cms.sso.client.util.UserThreadLocalFilter;
import com.nuanyou.cms.sso.client.validation.service.impl.TicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

/**
 * Created by Felix on 2017/3/20.
 */
@Configuration
public class ApplicationFilterConfiguration {

    @Value("${sso.urlPatterns}")
    private String urlPatterns;

    @Value("${sso.urlExcludePattern}")
    private String urlExcludePattern;

    @Value("${sso.reLogin}")
    private Boolean reLogin;

    @Value("${sso.loginUrl}")
    private String loginUrl;

    @Value("${sso.serverName}")
    private String serverName;

    @Value("${sso.validateCodeUrl}")
    private String validateCodeUrl;

    @Value("${sso.stateExpiredInMilliSeconds}")
    private Long stateExpiredInMilliSeconds;

    @Bean
    public FilterRegistrationBean singleSignOut(SignInOrSignOutFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        filter.setUrlExcludePattern(Pattern.compile(urlExcludePattern));
        registration.setFilter(filter);
        registration.addUrlPatterns(urlPatterns);
        registration.setName("SingleSignOutFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean authentication(AuthenticationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        filter.setUrlExcludePattern(Pattern.compile(urlExcludePattern));
        filter.setRelogin(reLogin);
        registration.setFilter(filter);
        registration.addInitParameter("loginUrl",loginUrl);
        registration.addInitParameter("serverName", serverName);
        registration.addUrlPatterns(urlPatterns);
        registration.setName("AuthenticationFilter");
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public FilterRegistrationBean ticketValidation(TicketValidationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.addInitParameter("validateCodeUrl", validateCodeUrl);
        registration.addInitParameter("serverName", serverName);
        registration.addInitParameter("needAutoLogOut",new Boolean(true).toString());
        registration.addUrlPatterns(urlPatterns);
        registration.setName("ticketValidationFilter");
        registration.setOrder(3);
        return registration;
    }

    @Bean
    public FilterRegistrationBean userThreadLocal(UserThreadLocalFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.addUrlPatterns(urlPatterns);
        registration.setName("userThreadLocalFilter");
        registration.setOrder(5);
        return registration;
    }

}

