package com.nuanyou.cms.config;

import com.nuanyou.cms.controller.PaymentOrderRecordController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Byron on 2017/6/13.
 */
@Configuration
@ComponentScan(basePackageClasses = PaymentOrderRecordController.class, useDefaultFilters = true)
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

    @Autowired
    public PaymentInterceptor paymentInterceptor;

    @Value("${paymentRecord.pattern-url}")
    private String paymentFilterPatternUrl;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(paymentInterceptor).addPathPatterns(paymentFilterPatternUrl);
        super.addInterceptors(registry);
    }
}
