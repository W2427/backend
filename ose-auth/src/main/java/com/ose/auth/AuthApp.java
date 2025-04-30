package com.ose.auth;

import com.ose.auth.domain.model.service.UserInterface;
import com.ose.auth.domain.model.service.UserService;
import com.ose.auth.dto.UserDTO;
import com.ose.controller.ServerController;
import com.ose.entity.BaseEntity;
import com.ose.util.CaptchaUtils;
import com.ose.util.ConsoleUtils;
import com.ose.util.MailUtils;
import com.ose.util.SMSUtils;
import com.ose.vo.EntityStatus;
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
 * 认证系统入口类。
 */
@SpringBootApplication//用于启动Spring Boot
@EntityScan("com.ose.auth")
@EnableJpaRepositories("com.ose.auth")
@ComponentScan({"com.ose"})
@EnableDiscoveryClient//启用Spring Cloud的服务，发现客户端，使当前服务能够注册到服务注册中心
@EnableFeignClients(basePackages = {
    "com.ose.auth.api",
})
public class AuthApp implements WebMvcConfigurer {

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
     * 创建系统用户账号。
     *
     * @param userService 用户操作服务
     */
    private static void createSystemUsers(UserInterface userService) {

        UserDTO userDTO;
        Long systemUserId = null;

        // 创建系统用户
        if (!userService.isUsernameAvailable("system")) {
            systemUserId = BaseEntity.generateId();
            userDTO = new UserDTO();
            userDTO.setType("system");
            userDTO.setName("System");
            userDTO.setUsername("system");
            userDTO.setStatus(EntityStatus.DISABLED);
            userService.create(systemUserId, systemUserId, userDTO);
            ConsoleUtils.log("user 'system' created");
        }

        // 若已创建系统用户则取得系统用户 ID
        if (systemUserId == null) {
            systemUserId = userService.getSystemUserId();
        }

        // 创建超级用户
        if (!userService.isUsernameAvailable("super")) {
            userDTO = new UserDTO();
            userDTO.setType("system");
            userDTO.setName("Super");
            userDTO.setUsername("super");
            userDTO.setPassword("super");
            userService.create(systemUserId, userDTO);
            ConsoleUtils.log("user 'super' created");
        }

    }

    /**
     * 认证系统入口方法。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) throws UnsupportedEncodingException {

        ConsoleUtils.log("OSE BPM Authentication Service starting...");

        SpringApplication application = new SpringApplication(AuthApp.class);

        application.addListeners(
            new ApplicationPidFileWriter("./ose-auth.pid")
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

        // 设置图形验证码字体文件路径
        CaptchaUtils.setFonts(
            environment.getProperty("application.resources.captcha-fonts")
        );

        // 创建系统用户账号
        createSystemUsers(context.getBean(UserService.class));

        ServerController.setApplicationContext(context);

        ConsoleUtils.log("OSE BPM Authentication Service started");
    }

}
