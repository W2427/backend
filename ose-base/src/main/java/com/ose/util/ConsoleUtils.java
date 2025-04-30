package com.ose.util;

import java.time.LocalDateTime;

/**
 * 控制台工具。
 */
public class ConsoleUtils {

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";

    /**
     * 生成时间戳。
     *
     * @return 时间戳
     */
    private static String timestamp() {
        return LocalDateTime.now().toString().replace('T', ' ');
    }

    /**
     * 打印日志。
     *
     * @param text      日志内容
     * @param foreColor 文字颜色
     * @param backColor 背景颜色
     */
    public static void log(String text, String foreColor, String backColor) {
        System.out.println(
            (new StringBuilder())
                .append(backColor)
                .append(foreColor)
                .append(timestamp())
                .append(" ")
                .append(text)
                .append(RESET)
        );
    }

    /**
     * 打印日志。
     *
     * @param text      日志内容
     * @param foreColor 文字颜色
     */
    public static void log(String text, String foreColor) {
        log(text, foreColor, CYAN_BACKGROUND);
    }

    /**
     * 打印日志。
     *
     * @param text 日志内容
     */
    public static void log(String text) {
        log(text, BLACK, CYAN_BACKGROUND);
    }

    /**
     * 打印警告信息。
     *
     * @param text 日志内容
     */
    public static void warn(String text) {
        log(text, BLACK, RED_BACKGROUND);
    }

}
