package com.ose.auth.config;

import com.ose.dto.ContextDTO;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .openapi("3.1.0")
            .info(new Info()
                .title("用户认证系统")
                .version("v0.0.1")
                .description("API 文档"));
    }

    /**
     * 配置 API 分组
     */
    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
            .group("ose-auth-api")
            .packagesToScan("com.ose.auth.controller")
            .pathsToMatch("/**")
            .addOpenApiCustomizer(contextDtoIgnorer())  // 调用自定义过滤逻辑
            .build();
    }

    /**
     * 自定义过滤逻辑（修复空指针）
     */
    private OpenApiCustomizer contextDtoIgnorer() {
        return openApi -> openApi.getPaths().values().stream()
            .flatMap(pathItem -> pathItem.readOperations().stream())
            .forEach(operation -> {
                // 🔨 修复点：初始化 parameters 列表（如果为 null）
                if (operation.getParameters() == null) {
                    operation.setParameters(new ArrayList<>());
                }
                // 安全移除 ContextDTO 参数
                operation.getParameters().removeIf(parameter ->
                    parameter.getSchema() != null &&
                        parameter.getSchema().getName() != null &&
                        parameter.getSchema().getName().equals(ContextDTO.class.getSimpleName())
                );
            });
    }
}
