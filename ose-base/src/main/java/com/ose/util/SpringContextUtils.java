package com.ose.util;

import cn.hutool.core.util.CharUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring Context 工具类
 *
 * @author wangzhen
 * @email zhen.wang2012@hotmail.com
 * @date 2016年11月29日 下午11:45:51
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
//        String[] names = applicationContext.getBeanDefinitionNames();
//        for (String nm : names) {
//            System.out.println(">>>>>>" + nm);
//        }
//        System.out.println("------\nBean 总计:" + applicationContext.getBeanDefinitionCount());
        if(CharUtil.isLetterUpper(name.charAt(1))) {
            name = StringUtils.upperFirst(name);
        }
        return applicationContext.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    public static Class<? extends Object> getType(String name) {
        return applicationContext.getType(name);
    }

}
