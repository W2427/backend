package com.ose.tasks.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description: Spring 上下文工具, 可用于获取spring 容器中的Bean
 * @author: zongf
 * @date: 2018-12-26 10:34
 */
@Component
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    /**
     * @param beanName bean名称
     * @Description: 获取spring容器中的bean, 通过bean名称获取
     * @return: Object 返回Object,需要做强制类型转换
     * @author: zongf
     * @time: 2018-12-26 10:45:07
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    /**
     * @param beanClass bean 类型
     * @Description: 获取spring容器中的bean, 通过bean类型获取
     * @return: T 返回指定类型的bean实例
     * @author: zongf
     * @time: 2018-12-26 10:46:31
     */
    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    /**
     * @param beanName  bean 名称
     * @param beanClass bean 类型
     * @Description: 获取spring容器中的bean, 通过bean名称和bean类型精确获取
     * @return: T 返回指定类型的bean实例
     * @author: zongf
     * @time: 2018-12-26 10:47:45
     */
    public static <T> T getBean(String beanName, Class<T> beanClass) {
        return applicationContext.getBean(beanName, beanClass);
    }


}
