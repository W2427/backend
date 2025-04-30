package com.ose.docs.config;

import com.ose.dto.ContextDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2 配置类。
 */
@Configuration
//@EnableSwagger2
@PropertySource("classpath:swagger.properties")
public class Swagger2Configuration {

    /**
     * 创建 RESTful API 说明文档。
     *
     * @return API 文档创建器
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .ignoredParameterTypes(ContextDTO.class)
            .apiInfo(
                new ApiInfoBuilder()
                    .title("文档管理系统")
                    .description("提供文档的上传、查询、下载服务。")
                    .version("v0.0.1")
                    .build()
            )
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.ose.docs.controller"))
            .paths(PathSelectors.any())
            .build();
    }

}
