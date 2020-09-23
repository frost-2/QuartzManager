package com.frost2.quartz.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author 陈伟平
 * @date 2019-12-02 11:33:00
 */
@Configuration
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.frost2.quartz.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("swagger文档")   //标题
                .description("quartzManager骨架项目") //描述
                .termsOfServiceUrl("http://www.nbiot10086.com") //这里配置的是服务网站
                .contact(new Contact("陈伟平", "", "1603153768@qq.com")) // 三个参数依次是姓名，个人网站，邮箱
                .version("0.0.1") //版本
                .build();
    }

}