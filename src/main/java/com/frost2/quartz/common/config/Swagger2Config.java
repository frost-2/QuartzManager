package com.frost2.quartz.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈伟平
 * @date 2019-12-02 11:33:00
 */
@Configuration
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {

        //在配置好的配置类中增加此段代码即可
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        Parameter randomStr = ticketPar.name("x-tit-noncestr").description("随机字符串")           //name表示名称，description表示描述
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true).defaultValue("randomStr")                  //required表示是否必填，defaultValue表示默认值
                .build();
        Parameter vStr = ticketPar.name("x-tit-vstr").description("sha1(randomStr+salt)")           //name表示名称，description表示描述
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true).defaultValue("5781392b15fb8ffbdb15664ab25b7675d3bb9ba5")                  //required表示是否必填，defaultValue表示默认值
                .build();
        pars.add(randomStr);    //添加完此处一定要把下边的带***的也加上否则不生效
        pars.add(vStr);    //添加完此处一定要把下边的带***的也加上否则不生效

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.frost2.quartz.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);   //***添加请求头
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("swagger文档")   //标题
                .description("宜家智能科技(广州)有限公司") //描述
                .termsOfServiceUrl("http://www.nbiot10086.com") //这里配置的是服务网站
                .contact(new Contact("宜家智能_陈伟平", "", "1603153768@qq.com")) // 三个参数依次是姓名，个人网站，邮箱
                .version("0.0.1") //版本
                .build();
    }

}