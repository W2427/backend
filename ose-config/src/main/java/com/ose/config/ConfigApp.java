package com.ose.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置服务。
 */
@SpringBootApplication
@ComponentScan({"com.ose"})
@EnableConfigServer
@EnableEurekaServer
public class ConfigApp implements WebMvcConfigurer {

    /**
     * 注册视图控制器。
     *
     * @param registry 注册器
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/summary").setViewName("redirect:/summary/api-docs/");
        registry.addViewController("/summary/").setViewName("redirect:/summary/api-docs/");
        registry.addViewController("/summary/sign-in").setViewName("redirect:/summary/sign-in/");
        registry.addViewController("/summary/sign-in/").setViewName("forward:/summary/sign-in/index.html");
        registry.addViewController("/summary/api-docs").setViewName("redirect:/summary/api-docs/");
        registry.addViewController("/summary/api-docs/").setViewName("forward:/summary/api-docs/index.html");
        registry.addViewController("/summary/services").setViewName("redirect:/summary/services/");
        registry.addViewController("/summary/services/").setViewName("forward:/summary/services/index.html");
    }

    /**
     * 注册静态资源。
     *
     * @param registry 注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/favicon.ico");
        registry.addResourceHandler("/summary/**").addResourceLocations("classpath:/summary/");
        //registry.addResourceHandler("/summary/**").addResourceLocations("file:///var/www/ose/backend/ose-config/src/main/resources/summary/");
    }

    /**
     * 配置服务入口方法。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(ConfigApp.class);

        application.addListeners(
            new ApplicationPidFileWriter("./ose-config.pid")
        );

        application.run(args);

        System.out.println("config server started");
    }

}
