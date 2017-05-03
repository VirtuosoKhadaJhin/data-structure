package com.nuanyou.cms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableAspectJAutoProxy
@SpringBootApplication
@EnableFeignClients
public class NuanyouMerchantApplication {

    private static final Logger log = LoggerFactory.getLogger(NuanyouMerchantApplication.class.getSimpleName());

    public static void main(String[] args) {
        SpringApplication.run(NuanyouMerchantApplication.class, args);
        System.out.println("start success! 启动成功！");
        log.warn("start success! 启动成功！");
    }
}
