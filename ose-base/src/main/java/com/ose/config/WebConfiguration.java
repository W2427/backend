package com.ose.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ose.response.JsonResponseError;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static org.springframework.core.io.support.ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;

/**
 * Web 配置。
 */
@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public static MessageSource messageSource() {
        return new CustomReloadableResourceBundleMessageSource();
    }

    @Bean
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Override
    @ConditionalOnMissingBean
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        /**
         * 序列换成json时,将所有的long变成string
         * 因为js中得数字类型不能包含所有的java long值
         */
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .addMixIn(JsonResponseError.class, JsonResponseErrorMixin.class);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
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

    /**
     * 从多个消息定义文件中读取消息定义的 MessageSource 实现。
     */
    @SuppressWarnings("NullableProblems")
    private static class CustomReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

        // 消息文件后缀名
        private static final String EXT_NAME = ".properties";

        // 路径格式匹配资源解析器
        private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        /**
         * 构造方法。
         * 默认加载多个 Class Path 下的消息资源定义文件，以 messages 作为文件名前缀，并使用 UTF-8 编码。
         */
        CustomReloadableResourceBundleMessageSource() {
            setBasename(CLASSPATH_ALL_URL_PREFIX + "messages");
            setDefaultEncoding("UTF-8");
        }

        /**
         * 刷新消息资源。
         * 重写超类方法以实现对多个 Class Path 下的消息资源文件的加载。
         *
         * @param filename         文件名
         * @param propertiesHolder 消息资源容器
         * @return 消息资源容器
         */
        @Override
        protected PropertiesHolder refreshProperties(String filename, PropertiesHolder propertiesHolder) {
            if (filename.startsWith(CLASSPATH_ALL_URL_PREFIX)) {
                return refreshClassPathProperties(filename, propertiesHolder);
            } else {
                return super.refreshProperties(filename, propertiesHolder);
            }
        }

        /**
         * 加载所有 Class Path 中的消息定义文件。
         *
         * @param filename         文件名
         * @param propertiesHolder 消息资源容器
         * @return 消息资源容器
         */
        private PropertiesHolder refreshClassPathProperties(String filename, PropertiesHolder propertiesHolder) {

            Properties properties = new Properties();
            long lastModified = -1;

            try {
                Resource[] resources = resolver.getResources(filename + EXT_NAME);
                for (Resource resource : resources) {
                    properties.putAll(Objects.requireNonNull(
                        super.refreshProperties(
                            resource.getURI().toString().replace(EXT_NAME, ""),
                            propertiesHolder
                        ).getProperties()
                    ));
                    if (lastModified < resource.lastModified()) {
                        lastModified = resource.lastModified();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }

            return new PropertiesHolder(properties, lastModified);
        }

    }



}
