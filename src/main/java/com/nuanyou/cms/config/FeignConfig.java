package com.nuanyou.cms.config;

import com.nuanyou.cms.remote.service.RemoteContractService;
import com.nuanyou.cms.util.JsonUtils;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Created by Felix on 2017/4/24.
 */
public class FeignConfig {

    @Value("${test}")
    private String host;
    
    

    @Bean
    public RemoteContractService contractService() {
        return Feign.builder()
                .decoder(new JacksonDecoder(JsonUtils.getMapper()))
//                .encoder()
                .target(RemoteContractService.class, host);
    }

}
