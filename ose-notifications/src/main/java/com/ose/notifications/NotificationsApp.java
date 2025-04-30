package com.ose.notifications;

import com.ose.controller.ServerController;
import com.ose.util.ConsoleUtils;
import com.ose.util.MailUtils;
import com.ose.util.SMSUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.UnsupportedEncodingException;

/**
 * 通知管理系统入口类。
 */
@SpringBootApplication
@EntityScan("com.ose.notifications")
@EnableJpaRepositories("com.ose.notifications")
@ComponentScan({"com.ose"})
@EnableFeignClients(basePackages = {
    "com.ose.auth.api",
    "com.ose.docs.api"
})
@EnableDiscoveryClient
public class NotificationsApp implements WebMvcConfigurer {

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
            .addResourceHandler("/docs/**")
            .addResourceLocations("file://" + swaggerUIDir);

    }

    /**
     * 通知管理入口方法。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) throws UnsupportedEncodingException {

        ConsoleUtils.log("OSE BPM Notification Service starting...");

        SpringApplication application = new SpringApplication(NotificationsApp.class);

        application.addListeners(
            new ApplicationPidFileWriter("./ose-notifications.pid")
        );

        ConfigurableApplicationContext context = application.run(args);
        Environment environment = context.getBean(Environment.class);

        // 初始化电子邮件发送工具
        MailUtils.init(
            context.getBean(JavaMailSender.class),
            environment.getProperty("spring.mail.username"),
            environment.getProperty("spring.application.name")
        );

        // 初始化短信发送工具
        SMSUtils.init(
            environment.getProperty("application.sms.send-uri"),
            environment.getProperty("application.sms.signature"),
            environment.getProperty("application.sms.account"),
            environment.getProperty("application.sms.password")
        );

        ServerController.setApplicationContext(context);

        ConsoleUtils.log("OSE BPM Notification Service started");
    }

}
