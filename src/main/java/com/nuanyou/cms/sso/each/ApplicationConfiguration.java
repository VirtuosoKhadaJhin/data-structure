package com.nuanyou.cms.sso.each;

import com.nuanyou.cms.sso.client.authentication.AuthenticationFilter;
import com.nuanyou.cms.sso.client.session.SingleSignOutFilter;
import com.nuanyou.cms.sso.client.util.UserThreadLocalFilter;
import com.nuanyou.cms.sso.client.util.HttpServletRequestWrapperFilter;
import com.nuanyou.cms.sso.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

/**
 * Created by Felix on 2017/3/20.
 */
@Configuration
public class ApplicationConfiguration {
    @Bean
    public FilterRegistrationBean testFilterRegistration(SingleSignOutFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        filter.setUrlExcludePattern(Pattern.compile("/test|^/dist.*|^/favicon.*"));
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.addInitParameter("artifactParameterName","code");
        registration.setName("Single Sign Out Filter");
        registration.setOrder(1);
        return registration;
    }


    @Bean
    public FilterRegistrationBean testFilterRegistration2(AuthenticationFilter authenticationFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        authenticationFilter.setUrlExcludePattern(Pattern.compile("/test|^/dist.*|^/favicon.*"));
        authenticationFilter.setRelogin(false);
        registration.setFilter(authenticationFilter);
        registration.addInitParameter("loginUrl","https://testuser.api.91nuanyou.com/oauth/corp");
        registration.addInitParameter("serverName","http://127.0.0.1:8085");
        registration.addInitParameter("serviceParameterName","ret");
        registration.addInitParameter("artifactParameterName","code");
        //registration.addInitParameter("encodeServiceUrl","true");
        registration.addUrlPatterns("/*");
        registration.setName("AuthenticationFilter");
        registration.setOrder(2);
        return registration;
    }


    @Bean
    public FilterRegistrationBean testFilterRegistration3(Cas20ProxyReceivingTicketValidationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        filter.setServerName("http://127.0.0.1:8085");
        registration.setFilter(filter);
        registration.addInitParameter("validateCodeUrl","https://testuser.api.91nuanyou.com/corp/oauth/access_token");
        registration.addInitParameter("serverName","http://127.0.0.1:8085");
        registration.addInitParameter("serviceParameterName","ret");
        registration.addInitParameter("artifactParameterName","code");
        registration.addUrlPatterns("/*");
        registration.setName("CAS Validation Filter");
        registration.setOrder(3);
        return registration;
    }


    @Bean
    public FilterRegistrationBean testFilterRegistration4(HttpServletRequestWrapperFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("CAS HttpServletRequest Wrapper Filter");
        registration.setOrder(4);
        return registration;
    }




    @Bean
    public FilterRegistrationBean testFilterRegistration5(UserThreadLocalFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("CAS Assertion Thread Local Filter");
        registration.setOrder(6);
        return registration;
    }

//    @Bean
//    public FilterRegistrationBean testFilterRegistration6() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(new UserFilter());
//        registration.addUrlPatterns("/*");
//        registration.setName("user filter");
//        registration.setOrder(5);
//        return registration;
//    }






}

