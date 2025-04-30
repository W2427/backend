package com.ose.util;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * 随机值工具。
 */
public class RandomUtils {

    // 随机数生成器
    private static final Random random = new SecureRandom();

    /**
     * 生成随机字节数组（取值范围为 0~255 的整数数组）。
     *
     * @param byteCount 字节数
     * @return 字节数组
     */
    public static int[] getBytes(int byteCount) {

        byte[] signed = new byte[byteCount];
        int[] unsigned = new int[byteCount];

        (new SecureRandom()).nextBytes(signed);

        for (short i = 0; i < byteCount; i++) {
            unsigned[i] = signed[i] + 128;
        }

        return unsigned;
    }

    /**
     * 生成十六进制随机字符串。
     *
     * @param byteCount 字节数
     * @return 十六进制随机字符串
     */
    public static String getHexString(int byteCount) {

        int[] bytes = getBytes(byteCount);

        StringBuilder sb = new StringBuilder();

        String hex;

        for (int b : bytes) {

            hex = Integer.toString(b, 16);

            if (hex.length() == 1) {
                sb.append("0");
            }

            sb.append(hex);
        }

        return sb.toString();
    }

    /**
     * 生成随机文本。
     *
     * @param charset 字符集
     * @param length  长度
     * @return 随机文本
     */
    public static String text(String charset, int length) {

        int charsetLength = charset.length();

        length = Math.min(Math.max(1, length), charsetLength);

        StringBuilder text = new StringBuilder();

        while (text.length() < length) {
            text.append(charset.charAt(random.nextInt(charsetLength)));
        }

        return text.toString();
    }

    /**
     * 取得指定范围的随机数。
     *
     * @param range 范围
     * @return 指定范围内的随机数
     */
    public static double between(double[] range) {
        return between(range[0], range[1]);
    }

    /**
     * 取得指定范围的随机数。
     *
     * @param from 最小值
     * @param to   最大值
     * @return 指定范围内的随机数
     */
    public static double between(double from, double to) {
        return between(from, to, 4);
    }

    /**
     * 取得指定范围的随机数。
     *
     * @param from    最小值
     * @param to      最大值
     * @param decimal 小数位
     * @return 指定范围内的随机数
     */
    public static double between(double from, double to, int decimal) {

        double base = Math.pow(10, Math.max(0, decimal));

        return Math.round(
            (from + Math.abs(random.nextDouble()) * (to - from)) * base
        ) / base;
    }

    /**
     * 取得指定范围的随机数。
     *
     * @param range 范围
     * @return 指定范围内的随机数
     */
    public static int between(int[] range) {
        return between(range[0], range[1]);
    }

    /**
     * 取得指定范围的随机数。
     *
     * @param from 最小值
     * @param to   最大值
     * @return 指定范围内的随机数
     */
    public static int between(int from, int to) {
        return from + random.nextInt(to - from);
    }

    /**
     * 取得指定范围的随机数。
     *
     * @param range 范围
     * @return 指定范围内的随机数
     */
    public static long between(long[] range) {
        return between(range[0], range[1]);
    }

    /**
     * 取得指定范围的随机数。
     *
     * @param from 最小值
     * @param to   最大值
     * @return 指定范围内的随机数
     */
    public static long between(long from, long to) {
        return Math.round(from + Math.abs(random.nextDouble()) * (to - from));
    }

    /**
     * 取得随机颜色。
     *
     * @param range 十六进制取值范围
     * @return 十六进制颜色值
     */
    public static String color(String[] range) {
        return color(range[0], range[1]);
    }

    /**
     * 取得随机颜色。
     *
     * @param from 十六进制取值范围
     * @param to   十六进制取值范围
     * @return 十六进制颜色值
     */
    public static String color(String from, String to) {
        return color(Integer.valueOf(from, 16), Integer.valueOf(to, 16));
    }

    /**
     * 取得随机颜色。
     *
     * @param range 十进制取值范围
     * @return 十六进制颜色值
     */
    public static String color(int[] range) {
        return color(range[0], range[1]);
    }

    /**
     * 取得随机颜色。
     *
     * @param from 十进制取值范围
     * @param to   十进制取值范围
     * @return 十六进制颜色值
     */
    public static String color(int from, int to) {
        return "#"
            + randomByte(from, to)
            + randomByte(from, to)
            + randomByte(from, to);
    }

    /**
     * 取得随机字节。
     *
     * @param from 十进制取值范围
     * @param to   十进制取值范围
     * @return 十六进制字节
     */
    public static String randomByte(int from, int to) {

        from = Math.min(Math.max(0, from), 255);
        to = Math.min(Math.max(0, to), 255);

        return StringUtils.last(
            "0" + Integer.toString(between(from, to), 16),
            2
        );

    }

    /**
     * 随机取得列表中的元素。
     *
     * @param <T>  范型
     * @param list 列表
     * @return 列表中的元素
     */
    public static <T> T randomItem(List<T> list) {
        return randomItem(list, null);
    }

    /**
     * 随机取得列表中的元素。
     *
     * @param <T>          范型
     * @param list         列表
     * @param defaultValue 默认值
     * @return 列表中的元素
     */
    public static <T> T randomItem(List<T> list, T defaultValue) {

        if (list == null) {
            return defaultValue;
        }

        int size = list.size();

        if (size == 0) {
            return defaultValue;
        }

        return list.get(random.nextInt(size));
    }

}
