package com.nuanyou.cms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunOSSConfiguration {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.oss.secret-access-key}")
    private String secretAccessKey;


    @Bean
    public AliyunOSService aliyunOSService() {
        return new AliyunOSService().setOSSClient(endpoint, accessKeyId, secretAccessKey);
    }

}