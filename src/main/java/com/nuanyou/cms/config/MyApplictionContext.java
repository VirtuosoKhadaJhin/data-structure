package com.nuanyou.cms.config;

import org.springframework.beans.BeansException;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Felix on 2016/9/12.
 */
@Configuration
@ServletComponentScan("com.nuanyou.cms")
public class MyApplictionContext implements ApplicationContextAware {

    public static ApplicationContext myapp = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        myapp = applicationContext;
    }
}
