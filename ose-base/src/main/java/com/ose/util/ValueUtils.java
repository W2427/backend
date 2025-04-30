package com.ose.util;

import java.util.function.Consumer;

/**
 * 值处理工具。
 */
public class ValueUtils {

    /**
     * 判断两个值是否相等，当都为空指针是视为相等。
     *
     * @param value1 值1
     * @param value2 值2
     * @return 是否相等
     */
    public static boolean equals(Object value1, Object value2) {
        return (value1 != null && value1.equals(value2))
            || (value1 == null && value2 == null);
    }

    /**
     * 当指定的值为空对象时返回默认值。
     *
     * @param <T>          值的类型
     * @param value        值
     * @param defaultValue 默认值
     * @return 值或默认值
     */
    public static <T> T ifNull(T value, T defaultValue) {
        return ifNull(value, defaultValue, value);
    }

    /**
     * 当指定的值为空对象时返回默认值，否则返回指定的值。
     *
     * @param <T>          值的类型
     * @param value        值
     * @param defaultValue 默认值
     * @param whenNotNull  指定的值
     * @return 指定的值或默认值
     */
    public static <T> T ifNull(Object value, T defaultValue, T whenNotNull) {
        return value == null ? defaultValue : whenNotNull;
    }

    /**
     * 返回值列表中第一个非空值。
     *
     * @param <T>    值的类型
     * @param values 值列表
     * @return 值列表中第一个非空值
     */
    public static <T> T notNull(T... values) {

        for (T value : values) {
            if (value != null) {
                return value;
            }
        }

        return null;
    }

    /**
     * 当值不为空指针时将值作为指定方法的参数执行。
     *
     * @param value    值
     * @param consumer 方法
     * @param <T>      值的类型
     */
    public static <T> void notNull(T value, Consumer<T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    /**
     * 返回值列表中第一个非空值。
     *
     * @param values 值列表
     * @return 值列表中第一个非空值
     */
    public static String notEmpty(String... values) {

        for (String value : values) {
            if (!StringUtils.isEmpty(value, true)) {
                return value;
            }
        }

        return null;
    }

}
