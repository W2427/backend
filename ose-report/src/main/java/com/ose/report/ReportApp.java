package com.ose.report;

import com.ose.controller.ServerController;
import com.ose.report.domain.service.ReportTemplateInterface;
import com.ose.report.domain.service.impl.ReportTemplateService;
import com.ose.report.dto.TemplateDTO;
import com.ose.report.vo.Domain;
import com.ose.report.vo.Position;
import com.ose.util.ConsoleUtils;
import com.ose.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 报表服务系统入口类。
 */
@SpringBootApplication
@EntityScan("com.ose.report")
@EnableJpaRepositories("com.ose.report")
@ComponentScan({"com.ose"})
@EnableFeignClients(basePackages = {
    "com.ose.auth.api",
    "com.ose.docs.api"
})
@EnableDiscoveryClient
public class ReportApp implements WebMvcConfigurer {

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

        registry
            .addViewController("/samples/statistics")
            .setViewName("redirect:/samples/statistics/");

        registry
            .addViewController("/samples/statistics/")
            .setViewName("forward:/samples/statistics/index.html");
    }

    /**
     * 注册静态资源。
     *
     * @param registry 注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        final Set<String> activeProfiles = new HashSet<>(Arrays.asList(
            SpringContextUtils
                .applicationContext
                .getEnvironment()
                .getActiveProfiles()
        ));

        registry
            .addResourceHandler("/favicon.ico")
            .addResourceLocations("classpath:/favicon.ico");

        registry
            .addResourceHandler("/developer/**")
            .addResourceLocations("classpath:/developer/");

        registry
            .addResourceHandler("/specifications/**")
            .addResourceLocations("classpath:/specifications/");

        if (activeProfiles.contains("production") || activeProfiles.contains("staging")) {
            registry
                .addResourceHandler("/samples/**")
                .addResourceLocations("classpath:/samples/");
        } else {
            registry
                .addResourceHandler("/samples/**")
                .addResourceLocations("file:///var/www/ose/src/ose-report/src/main/resources/samples/");
        }

        registry
            .addResourceHandler("/docs/**")
            .addResourceLocations("file://" + swaggerUIDir);
    }

    /**
     * 初始化数据。
     *
     * @param context Spring Context
     */
    private static void createInitializationData(ConfigurableApplicationContext context) {

        ReportTemplateInterface reportTemplateService = context.getBean(ReportTemplateService.class);

        TemplateDTO templateDTO = new TemplateDTO();
        String templateName = "检查单通用表头模板";

        if (!reportTemplateService.isNameAvailable(templateName)) {
            templateDTO.setName(templateName);
            templateDTO.setDomain(Domain.CHECKLIST);
            templateDTO.setPosition(Position.TITLE);
            templateDTO.setFixedHeight(85);
            templateDTO.setTemplateFile("page_title.jrxml");
            reportTemplateService.create(templateDTO);
        }

        templateName = "检查单通用签字栏模板";

        if (!reportTemplateService.isNameAvailable(templateName)) {
            templateDTO.setName(templateName);
            templateDTO.setDomain(Domain.CHECKLIST);
            templateDTO.setPosition(Position.SIGNATURE);
            templateDTO.setFixedHeight(75);
            templateDTO.setTemplateFile("page_signature.jrxml");
            reportTemplateService.create(templateDTO);
        }
    }

    /**
     * 报表服务系统入口方法。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {

        ConsoleUtils.log("OSE BPM Report Service starting...");

        SpringApplication application = new SpringApplication(ReportApp.class);

        application.addListeners(
            new ApplicationPidFileWriter("./ose-report.pid")
        );

        ConfigurableApplicationContext context = application.run(args);

        // 初始化数据
        createInitializationData(context);

        ServerController.setApplicationContext(context);

        ConsoleUtils.log("OSE BPM Report Service started");
    }
}
