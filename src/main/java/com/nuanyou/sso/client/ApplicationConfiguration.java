package com.nuanyou.sso.client;

import com.nuanyou.sso.client.authentication.AuthenticationFilter;
import com.nuanyou.sso.client.session.SingleSignOutFilter;
import com.nuanyou.sso.client.util.UserThreadLocalFilter;
import com.nuanyou.sso.client.util.HttpServletRequestWrapperFilter;
import com.nuanyou.sso.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

/**
 * Created by Felix on 2017/3/20.
 */
@Configuration
public class ApplicationConfiguration {

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
        registration.setName("Single Sign Out Filter");
        registration.setOrder(1);
        return registration;
    }


    @Bean
    public FilterRegistrationBean testFilterRegistration2(AuthenticationFilter authenticationFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        authenticationFilter.setUrlExcludePattern(Pattern.compile("/test|^/dist.*|^/favicon.*"));
        authenticationFilter.setRelogin(reLogin);
        registration.setFilter(authenticationFilter);
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
    public FilterRegistrationBean testFilterRegistration3(Cas20ProxyReceivingTicketValidationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        filter.setServerName(serverName);
        registration.setFilter(filter);
        registration.addInitParameter("validateCodeUrl", validateCodeUrl);
        registration.addInitParameter("serverName", serverName);
        registration.addInitParameter("serviceParameterName", "ret");
        registration.addInitParameter("artifactParameterName", "code");
        registration.addUrlPatterns(urlPatterns);
        registration.setName("CAS Validation Filter");
        registration.setOrder(3);
        return registration;
    }


    @Bean
    public FilterRegistrationBean testFilterRegistration4(HttpServletRequestWrapperFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.addUrlPatterns(urlPatterns);
        registration.setName("CAS HttpServletRequest Wrapper Filter");
        registration.setOrder(4);
        return registration;
    }


    @Bean
    public FilterRegistrationBean testFilterRegistration5(UserThreadLocalFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.addUrlPatterns(urlPatterns);
        registration.setName("CAS Assertion Thread Local Filter");
        registration.setOrder(5);
        return registration;
    }





}

