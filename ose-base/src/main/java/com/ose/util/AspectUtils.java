package com.ose.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 切面工具。
 */
public class AspectUtils {

    /**
     * 被切入方法参数的容器。
     */
    public static class ParameterHolder {

        private final Map<String, Object> parameters = new HashMap<>();

        public ParameterHolder set(String name, Object value) {
            parameters.put(name, value);
            return this;
        }

        public Object get(String name) {
            return parameters.get(name);
        }

        public String getAsString(String name) {
            if(get(name) == null) {
                return null;
            } else if(get(name) instanceof String) {
                return (String) get(name);
            } else {
                return ((Long) get(name)).toString();
            }
//            return get(name) == null ? null : (String) get(name);
        }

    }

    /**
     * 取得切入点方法的参数的值。
     *
     * @param point 切入点
     * @param names 参数名
     * @return 参数名称与值的映射表
     */
    public static ParameterHolder getParameters(JoinPoint point, String... names) {

        ParameterHolder result = new ParameterHolder();
        Set<String> nameSet = new HashSet<>(Arrays.asList(names));

        MethodSignature signature = (MethodSignature) point.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        String[] parameterNames = signature.getParameterNames();
        Object[] parameterValues = point.getArgs();
        Parameter parameter;
        String parameterName;
        Object parameterValue;

        for (int index = 0; index < parameters.length; index++) {

            parameter = parameters[index];
            parameterName = parameterNames[index];
            parameterValue = parameterValues[index];

            if (parameter.getAnnotation(PathVariable.class) == null
                || !(parameterValue instanceof String || parameterValue instanceof Long)) {
                continue;
            }

            if (nameSet.remove(parameterName)) {
                result.set(parameterName, parameterValue);
            }

        }

        return result;
    }

}
