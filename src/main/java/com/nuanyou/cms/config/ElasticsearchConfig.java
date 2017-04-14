package com.nuanyou.cms.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.host}")
    private String host;

    @Bean
    public RestClient restClient() {
        RestClient restClient = RestClient.builder(new HttpHost(host, 80, "http")).build();
        return restClient;
    }

}