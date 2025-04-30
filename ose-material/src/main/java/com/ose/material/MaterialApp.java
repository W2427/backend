package com.ose.material;


import com.ose.controller.ServerController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import com.ose.util.ConsoleUtils;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executor;

/**
 * 材料系统入口
 */
@SpringBootApplication
@EntityScan("com.ose.material")
@EnableJpaRepositories("com.ose.material")
@ComponentScan({"com.ose"})
@EnableAsync
@EnableFeignClients(basePackages = {
    "com.ose.auth.api",
    "com.ose.docs.api"
})
@EnableDiscoveryClient
@EnableBatchProcessing
@EnableScheduling
public class MaterialApp implements WebMvcConfigurer {

    // Swagger UI 路径
    @Value("${application.resources.swagger-ui}")
    private String swaggerUIDir;

    /**
     * 注册视图控制器。
     *
     * @param registry 注册器
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry
            .addViewController("/developer")
            .setViewName("redirect:/developer/");

        registry
            .addViewController("/developer/")
            .setViewName("forward:/developer/index.md");

        registry
            .addViewController("/docs")
            .setViewName("redirect:/docs/");

        registry
            .addViewController("/docs/")
            .setViewName("forward:/docs/index.html");

    }

    /**
     * 注册静态资源。
     *
     * @param registry 注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry
            .addResourceHandler("/favicon.ico")
            .addResourceLocations("classpath:/favicon.ico");

        registry
            .addResourceHandler("/developer/**")
            .addResourceLocations("classpath:/developer/");

        registry
            .addResourceHandler("/docs/**")
            .addResourceLocations("file://" + swaggerUIDir);
        registry
            .addResourceHandler("/templates/**")
            .addResourceLocations("classpath:/templates/");

    }

    /**
     * OSE材料系统入口方法。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) throws UnsupportedEncodingException {

        ConsoleUtils.log("OSE Material Service starting...");

        SpringApplication application = new SpringApplication(MaterialApp.class);

        application.addListeners(
            new ApplicationPidFileWriter("./ose-material.pid")
        );
        ConfigurableApplicationContext context = application.run(args);
        Environment environment = context.getBean(Environment.class);

        ServerController.setApplicationContext(context);

        ConsoleUtils.log("OSE Material Service started");
    }

    @EnableAsync
    @Configuration
    class TaskPoolConfig {

        /**
         * 通用线程池。
         *
         * @return
         */
        @Bean("materialExecutor")
        public Executor materialExecutor() {
            ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
            executor.setPoolSize(300);
            executor.setThreadNamePrefix("materialExecutor-");

            executor.setWaitForTasksToCompleteOnShutdown(true);

            executor.setAwaitTerminationSeconds(60);
            return executor;
        }

    }

}
