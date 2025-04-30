package com.ose.util;

import com.ose.exception.BusinessError;
import com.ose.exception.ValidationError;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

/**
 * Bean 工具。
 */
public class BeanUtils {

    /**
     * 复制对象属性。
     *
     * @param map    源数据对象
     * @param keys   字段名
     * @param target 目标对象
     * @param type   目标对象的类型
     * @param <T>    目标对象范型
     */
    private static <T> void copyProperties(
        final Map<String, Object> map,
        final Set<String> keys,
        final T target,
        final Class<?> type
    ) {
        if (keys.size() == 0 || type == Object.class) {
            return;
        }

        String key;
        Object value;
        Class<?> fieldType;

        for (Field field : type.getDeclaredFields()) {

            key = field.getName();
            value = map.get(key);
            fieldType = field.getType();

            if (!keys.remove(key)) {
                continue;
            }

            field.setAccessible(true);

            try {
                if (value == null || fieldType.isInstance(value)) {
                    field.set(target, value);
                } else if (fieldType == String.class) {
                    field.set(target, String.valueOf(value));
                } else if (fieldType == Integer.class || fieldType == int.class) {
                    field.set(target, Integer.valueOf(String.valueOf(value)));
                } else if (fieldType == Double.class || fieldType == double.class) {
                    field.set(target, Double.valueOf(String.valueOf(value)));
                } else if (fieldType == Long.class || fieldType == long.class) {
                    field.set(target, Long.valueOf(String.valueOf(value)));
                } else if (fieldType == BigDecimal.class) {
                    field.set(target, BigDecimal.valueOf(Long.valueOf(String.valueOf(value))));
                } else {
                    field.set(target, value);
                }
            } catch (IllegalArgumentException e) {
                throw new ValidationError(String.format(
                    "cannot cast value \"%s\" to type \"%s\"",
                    String.valueOf(value),
                    fieldType.toString()
                ));
            } catch (IllegalAccessException e) {
                // nothing to do
            }
        }

        copyProperties(map, keys, target, type.getSuperclass());
    }

    /**
     * 复制对象属性。
     *
     * @param map  源数据对象
     * @param type 目标对象类型
     * @return 目标对象
     */
    public static <T> T copyProperties(
        final Map<String, Object> map,
        final Class<T> type
    ) {
        try {
            return copyProperties(map, type.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    /**
     * 复制对象属性。
     *
     * @param map    源数据对象
     * @param target 目标对象
     * @return 目标对象
     */
    public static <T> T copyProperties(
        final Map<String, Object> map,
        final T target
    ) {
        return copyProperties(map, target, new String[]{});
    }

    /**
     * 下划线转驼峰
     *
     * @param map
     * @return
     */

    public static Map<String, Object> toReplaceKeyLow(Map<String, Object> map) {
        Map re_map = new HashMap();
        if (map != null) {
            Iterator var2 = map.entrySet().iterator();

            while (var2.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) var2.next();
                re_map.put(StringUtils.underscoreToCamelCase((String) entry.getKey()), map.get(entry.getKey()));
            }

            map.clear();
        }

        return re_map;
    }

    public static Map<String, Object> toReplaceKeyLowWithNoChange(Map<String, Object> map) {
        Map re_map = new HashMap();
        if (map != null) {
            Iterator var2 = map.entrySet().iterator();

            while (var2.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) var2.next();
                re_map.put(StringUtils.underscoreToCamelCase((String) entry.getKey()), map.get(entry.getKey()));
            }

        }

        return re_map;
    }

    /**
     * 复制对象属性。
     *
     * @param map              源数据对象
     * @param target           目标对象
     * @param ignoreProperties 忽略的属性
     * @return 目标对象
     */
    public static <T> T copyProperties(
        final Map<String, Object> map,
        final T target,
        final String... ignoreProperties
    ) {
        if (map == null || map.size() == 0 || target == null) {
            return target;
        }

        Set<String> keys = map.keySet();
        keys.removeAll(Arrays.asList(ignoreProperties));

        copyProperties(map, keys, target, target.getClass());

        return target;
    }

    /**
     * 复制对象属性。
     *
     * @param source 源数据对象
     * @param target 目标对象
     * @return 目标对象
     */
    public static <T> T copyProperties(
        final Object source,
        final T target
    ) {
        return copyProperties(source, target, new String[]{});
    }

    /**
     * 复制对象属性。
     *
     * @param source           源数据对象
     * @param target           目标对象
     * @param ignoreProperties 忽略的属性
     * @return 目标对象
     */
    public static <T> T copyProperties(
        final Object source,
        final T target,
        final String... ignoreProperties
    ) {
        return copyProperties(source, target, Arrays.asList(ignoreProperties));
    }

    /**
     * 复制对象属性。
     *
     * @param source           源数据对象
     * @param target           目标对象
     * @param ignoreProperties 忽略的属性
     * @return 目标对象
     */
    public static <T> T copyProperties(
        final Object source,
        final T target,
        final Collection<String> ignoreProperties
    ) {
        org.springframework.beans.BeanUtils.copyProperties(
            source,
            target,
            (ignoreProperties == null || ignoreProperties.size() == 0)
                ? (new String[]{})
                : ignoreProperties.toArray(new String[]{})
        );
        return target;
    }

    /**
     * 复制对象。
     *
     * @param <T>        范型
     * @param source     源数据对象
     * @param targetType 目标类型
     * @return 复制后的对象
     */
    public static <T> T clone(Object source, Class<T> targetType) {
        try {
            return copyProperties(source, targetType.newInstance());
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    /**
     * 转换列表中元素的类型。
     *
     * @param <T>     目标类型
     * @param sources 原始列表
     * @param type    目标类型
     * @return 转换后的列表
     */
    public static <T> List<T> convertType(
        final List<?> sources,
        final Class<T> type
    ) {

        if (sources == null) {
            return null;
        }

        List<T> targets = new ArrayList<>();

        if (sources.size() == 0) {
            return targets;
        }

        for (Object source : sources) {
            try {
                targets.add(copyProperties(source, type.newInstance()));
            } catch (ReflectiveOperationException e) {
                throw new BusinessError("error.internal");
            }
        }

        return targets;
    }

    /**
     * 生成类型转换函数。
     *
     * @param <S>        原始类型
     * @param <T>        目标类型
     * @param targetType 目标类型
     * @return 类型转换函数
     */
    public static <S, T> Function<S, T> converter(final Class<T> targetType) {
        return source -> {
            try {
                return copyProperties(source, targetType.newInstance());
            } catch (ReflectiveOperationException e) {
                return null;
            }
        };
    }

    /**
     * 复制对象属性到映射表。
     *
     * @param source 对象
     * @param target 属性名与属性值的映射表
     * @param type   对象的类型
     */
    private static void copyProperties(final Object source, final Map<String, Object> target, final Class<?> type) {

        if (type == Object.class) {
            return;
        }

        for (Field field : type.getDeclaredFields()) {

            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            try {
                field.setAccessible(true);
                final Object fieldValue = field.get(source);
                target.putIfAbsent(field.getName(), fieldValue);
            } catch (IllegalAccessException e) {
                // nothing to do
            }

        }

        copyProperties(source, target, type.getSuperclass());
    }

    /**
     * 复制对象属性到映射表。
     *
     * @param source 对象
     * @param target 属性名与属性值的映射表
     */
    public static void copyProperties(final Object source, final Map<String, Object> target) {

        if (source == null || target == null) {
            return;
        }

        copyProperties(source, target, source.getClass());
    }


    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }
}
