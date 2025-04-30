package com.ose.aspect;

import com.ose.annotation.DoNotTrimString;
import com.ose.dto.BaseDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 去除 DTO 字符串属性的首尾空白字符。
 */
@Aspect
@Component
public class TrimControllerArgumentAspect extends BaseAspect {

    /**
     * 定义切入点：DTO 的 set 方法，且参数类型为字符串、字符串数组或字符串集合。
     */
    @Pointcut(
        "execution(public com.ose.response.* com.ose..*.controller..*.*(..))"
            + " && !@annotation(org.springframework.web.bind.annotation.ExceptionHandler)"
            + " && !@annotation(com.ose.annotation.DoNotTrimString)"
    )
    public void controller() {
    }

    /**
     * 去除字符串类型值的首尾空白字符。
     */
    @SuppressWarnings("unchecked")
    @Before(value = "controller()", argNames = "point")
    public void doBefore(JoinPoint point) {

        Parameter[] parameters = ((MethodSignature) point.getSignature()).getMethod().getParameters();
        Object[] arguments = point.getArgs();

        for (int i = 0; i < arguments.length; i++) {
            if (parameters[i].getAnnotation(DoNotTrimString.class) != null) {
                continue;
            }
            arguments[i] = trim(arguments[i]);
        }
    }

    /**
     * 去除字符串类型值或对象的字符串属性值的首尾空格。
     *
     * @param argument 控制器方法参数
     * @return 处理后的参数值
     */
    private Object trim(final Object argument) {

        if (argument == null) {
            return null;
        }

        if (argument instanceof BaseDTO) {
            return trim((BaseDTO) argument, argument.getClass());
        }

        if (argument instanceof String) {
            return ((String) argument).trim();
        }

        if (argument instanceof Object[]) {
            return trim((Object[]) argument);
        }

        if (argument instanceof List) {
            return trim((List) argument);
        }

        if (argument instanceof Set) {
            return trim((Set) argument);
        }

        return argument;
    }

    /**
     * 去除 DTO 的字符串属性首尾空格。
     *
     * @param dto  DTO 对象
     * @param type DTO 对象的类型
     * @return 处理后的 DTO 对象
     */
    private Object trim(final BaseDTO dto, Class<?> type) {

        if (type == null) {
            return dto;
        }

        if (type.getAnnotation(DoNotTrimString.class) != null) {
            return dto;
        }

        Field[] fields = type.getDeclaredFields();

        for (Field field : fields) {

            if (field.getAnnotation(DoNotTrimString.class) != null) {
                continue;
            }

            field.setAccessible(true);

            try {
                field.set(dto, trim(field.get(dto)));
            } catch (IllegalAccessException e) {
                // nothing to do
            }
        }

        return trim(dto, type.getSuperclass());
    }

    /**
     * 去除数组中字符串的首尾空格。
     *
     * @param values 数组
     * @return 处理后的值
     */
    private Object trim(final Object[] values) {

        if (values.length == 0) {
            return values;
        }

        Object value;

        for (int i = 0; i < values.length; i++) {
            value = values[i];
            if (value instanceof String) {
                values[i] = ("" + value).trim();
            } else if (value instanceof BaseDTO) {
                values[i] = trim((BaseDTO) value, value.getClass());
            }
        }

        return values;
    }

    /**
     * 去除列表中字符串的首尾空格。
     *
     * @param values 列表
     * @return 处理后的值
     */
    @SuppressWarnings("unchecked")
    private Object trim(final List values) {

        if (values.size() == 0) {
            return values;
        }

        Object value;

        for (int i = 0; i < values.size(); i++) {
            value = values.get(i);
            if (value instanceof String) {
                values.set(i, ("" + value).trim());
            } else if (value instanceof BaseDTO) {
                values.set(i, trim((BaseDTO) value, value.getClass()));
            }
        }

        return values;
    }

    /**
     * 去除集合中字符串的首尾空格。
     *
     * @param values 集合
     * @return 处理后的值
     */
    @SuppressWarnings("unchecked")
    private Object trim(final Set values) {

        if (values.size() == 0) {
            return values;
        }

        final HashSet<Object> modified = new HashSet<>();

        values.forEach(value -> {
            if (value instanceof String) {
                modified.add(("" + value).trim());
            } else if (value instanceof BaseDTO) {
                modified.add(trim((BaseDTO) value, value.getClass()));
            }
        });

        values.clear();
        values.addAll(modified);

        return values;
    }

}
