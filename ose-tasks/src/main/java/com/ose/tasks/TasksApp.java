package com.ose.tasks;

import com.ose.controller.ServerController;
import com.ose.util.ConsoleUtils;
import com.ose.util.MailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.UnsupportedEncodingException;

import java.util.concurrent.Executor;

/**
 * 任务管理系统入口类。
 */
@SpringBootApplication
@EntityScan("com.ose.tasks")
@EnableJpaRepositories("com.ose.tasks")
@ComponentScan({"com.ose"})
@EnableAsync
@EnableFeignClients(basePackages = {
    "com.ose.auth.api",
    "com.ose.docs.api",
    "com.ose.notifications.api",
    "com.ose.report.api",
    "com.ose.material.api",
    "com.ose.issues.api"
})
@EnableDiscoveryClient
@EnableBatchProcessing
@EnableScheduling
public class TasksApp implements WebMvcConfigurer {

    private final static Logger logger = LoggerFactory.getLogger(TasksApp.class);

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

        registry
            .addResourceHandler("/samples/**")
            .addResourceLocations("classpath:/samples/");
        registry
            .addResourceHandler("/templates/**")
            .addResourceLocations("classpath:/templates/");

    }

    /**
     * 任务管理入口方法。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) throws UnsupportedEncodingException {

        ConsoleUtils.log("OSE BPM Task Service starting...");

        SpringApplication application = new SpringApplication(TasksApp.class);
        application.addListeners(new ApplicationPidFileWriter("./ose-tasks.pid"));
        ConfigurableApplicationContext context = application.run(args);
        Environment environment = context.getBean(Environment.class);


        MailUtils.init(
            context.getBean(JavaMailSender.class),
            environment.getProperty("spring.mail.username"),
            environment.getProperty("spring.application.name")
        );

        ServerController.setApplicationContext(context);

        ConsoleUtils.log("OSE BPM Task Service started");
    }

    @EnableAsync
    @Configuration
    class TaskPoolConfig {

        /**
         * 通用线程池。
         *
         * @return
         */
        @Bean("taskExecutor")
        public Executor taskExecutor() {
            ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
            executor.setPoolSize(300);
            executor.setThreadNamePrefix("taskExecutor-");

            executor.setWaitForTasksToCompleteOnShutdown(true);
            executor.setAwaitTerminationSeconds(600);
            executor.setErrorHandler(throwable -> logger.error("调度任务发生异常", throwable));
            return executor;
        }

        /**
         * 图纸打包线程池。
         *
         * @return
         */
        @Bean("drawingTaskExecutor")
        public Executor drawingTaskExecutor() {
            ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
            executor.setPoolSize(100);
            executor.setThreadNamePrefix("drawingTaskExecutor-");

            executor.setWaitForTasksToCompleteOnShutdown(true);
            executor.setAwaitTerminationSeconds(600);
            executor.setErrorHandler(throwable -> logger.error("调度任务发生异常", throwable));
            return executor;
        }

        /**
         * 建造流程线程池。
         *
         * @return
         */
        @Bean("constructTaskExecutor")
        public Executor constructTaskExecutor() {
            ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
            executor.setPoolSize(100);
            executor.setThreadNamePrefix("constructTaskExecutor-");

            executor.setWaitForTasksToCompleteOnShutdown(true);

            executor.setAwaitTerminationSeconds(600);
            executor.setErrorHandler(throwable -> logger.error("调度任务发生异常", throwable));
            return executor;
        }

    }

}
