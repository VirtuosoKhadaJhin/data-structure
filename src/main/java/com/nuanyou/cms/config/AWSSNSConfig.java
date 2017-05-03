package com.nuanyou.cms.config;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ICQ on 27/10/2016.
 */
@Configuration
public class AWSSNSConfig {
    @Value("${AWS.region}")
    private String region;

    @Bean
    AmazonSNSClient amazonSNSClient(){
        AmazonSNSClient snsClient = new AmazonSNSClient();
        try {
            snsClient.setRegion(Region.getRegion(Regions.fromName(region)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  snsClient;
    }
}
