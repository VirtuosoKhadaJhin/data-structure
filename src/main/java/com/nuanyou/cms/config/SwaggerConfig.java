package com.nuanyou.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;


/**
 * Created by GENIUS on 2017/9/20.
 */
@Configuration
public class SwaggerConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
    }

    ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("CMS MERCHANT").description("暖游CMS").license("").licenseUrl("").termsOfServiceUrl("").version("1.0.0")
                .contact(new Contact("", "", "")).build();
    }

    @Bean
    public Docket customImplementation() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.nuanyou.cms.controller"))
                .build().apiInfo(apiInfo());
        return docket;
    }
}