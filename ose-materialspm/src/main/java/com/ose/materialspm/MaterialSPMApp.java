package com.ose.materialspm;


import com.ose.controller.ServerController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ose.util.ConsoleUtils;

/**
 * OSE材料系统入口
 */
@SpringBootApplication
@EntityScan("com.ose.materialspm")
@EnableJpaRepositories("com.ose.materialspm")
@ComponentScan({"com.ose"})
@EnableFeignClients(basePackages = {
    "com.ose.auth.api",
    "com.ose.docs.api"
})
@EnableDiscoveryClient
public class MaterialSPMApp implements WebMvcConfigurer {

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

    }

    /**
     * OSE材料系统入口方法。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {

        ConsoleUtils.log("OSE BPM Material Service starting...");

        SpringApplication application = new SpringApplication(MaterialSPMApp.class);

        application.addListeners(
            new ApplicationPidFileWriter("./ose-materialspm.pid")
        );

        ServerController.setApplicationContext(application.run(args));

        ConsoleUtils.log("OSE BPM Material Service started");
    }
}
