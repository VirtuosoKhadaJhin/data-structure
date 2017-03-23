package com.nuanyou.cms.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

/**
 * Created by kevin.shu on 9/01/2016.
 */
@Configuration
@ServletComponentScan("com.nuanyou")
public class JPAConfiguration {
    @Bean
    public AuditorAware nullAuditBean() {
        return new AuditorAware() {
            @Override
            public Object getCurrentAuditor() {
                return null;
            }
        };
    }
}
