package com.nuanyou.cms.sso.client;

import com.nuanyou.cms.sso.client.authentication.AuthenticationFilter;
import com.nuanyou.cms.sso.client.session.SingleSignOutFilter;
import com.nuanyou.cms.sso.client.util.HttpServletRequestWrapperFilter;
import com.nuanyou.cms.sso.client.util.UserThreadLocalFilter;
import com.nuanyou.cms.sso.client.validation.TicketValidationFilter;
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
    public FilterRegistrationBean testFilterRegistration(SingleSignOutFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        filter.setUrlExcludePattern(Pattern.compile(urlExcludePattern));
        registration.setFilter(filter);
        registration.addUrlPatterns(urlPatterns);
        registration.addInitParameter("artifactParameterName", "code");
        registration.setName("SingleSignOutFilter");
        registration.setOrder(1);
        return registration;
    }


    @Bean
    public FilterRegistrationBean testFilterRegistration2(AuthenticationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        filter.setUrlExcludePattern(Pattern.compile(urlExcludePattern));
        filter.setRelogin(reLogin);
        registration.setFilter(filter);
        registration.addInitParameter("loginUrl",loginUrl);
        registration.addInitParameter("serverName", serverName);
        registration.addInitParameter("serviceParameterName", "ret");
        registration.addInitParameter("artifactParameterName", "code");
        registration.addUrlPatterns(urlPatterns);
        registration.setName("AuthenticationFilter");
        registration.setOrder(2);
        return registration;
    }


    @Bean
    public FilterRegistrationBean testFilterRegistration3(TicketValidationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        filter.setServerName(serverName);
        registration.setFilter(filter);
        registration.addInitParameter("validateCodeUrl", validateCodeUrl);
        registration.addInitParameter("serverName", serverName);
        registration.addInitParameter("serviceParameterName", "ret");
        registration.addInitParameter("artifactParameterName", "code");
        registration.addUrlPatterns(urlPatterns);
        registration.setName("TicketValidationFilter");
        registration.setOrder(3);
        return registration;
    }


    @Bean
    public FilterRegistrationBean testFilterRegistration4(HttpServletRequestWrapperFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.addUrlPatterns(urlPatterns);
        registration.setName("HttpServletRequestWrapperFilter");
        registration.setOrder(4);
        return registration;
    }


    @Bean
    public FilterRegistrationBean testFilterRegistration5(UserThreadLocalFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.addUrlPatterns(urlPatterns);
        registration.setName("userThreadLocalFilter");
        registration.setOrder(5);
        return registration;
    }





}

