package com.ose.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ose.response.JsonResponseError;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    /**
     * JSON 序列化配置。
     */
    @Bean
    public ObjectMapper objectMapper() {
        return (new ObjectMapper())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .addMixIn(JsonResponseError.class, JsonResponseErrorMixin.class);
    }





    /**
     * 不返回错误详细信息到客户端。
     */
    private static abstract class JsonResponseErrorMixin {

        @JsonIgnore
        public abstract Throwable getCause();

        @JsonIgnore
        public abstract StackTraceElement[] getStackTrace();

        @JsonIgnore
        public abstract String getLocalizedMessage();

        @JsonIgnore
        public abstract Throwable[] getSuppressed();
    }

}
