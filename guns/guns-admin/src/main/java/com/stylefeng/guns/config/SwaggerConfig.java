package com.stylefeng.guns.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置类
 *
 * @author fengshuonan
 * @date 2017年6月1日19:42:59
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "guns", name = "swagger-open", havingValue = "true")
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))                         //这里采用包含注解的方式来确定要显示的接口
                //.apis(RequestHandlerSelectors.basePackage("com.stylefeng.guns.modular.system.controller"))    //这里采用包扫描的方式来确定要显示的接口
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Guns Doc")
                .description("Guns Api文档")
                .termsOfServiceUrl("http://git.oschina.net/naan1993/guns")
                .contact("stylefeng")
                .version("2.0")
                .build();
    }

    @Bean
    public Docket customerApi() {
        String title = "客户模块";
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(title)
                .select()
                .paths(PathSelectors.regex("/customer/.*"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket orderApi() {
        String title = "订单模块";
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(title)
                .select()
                .paths(PathSelectors.regex("/order/.*"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket bankApi() {
        String title = "财务模块";
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(title)
                .select()
                .paths(PathSelectors.regex("/bank/.*"))
                .paths(PathSelectors.any())
                .build();
    }
}
