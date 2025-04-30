package com.ose.util;

import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志输出工具。
 */
public class Logger {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private final org.slf4j.Logger logger;

    /**
     * 构造方法。
     *
     * @param clazz 输出日志的类
     */
    public Logger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    /**
     * 输出信息。
     *
     * @param content 日志内容
     */
    public void info(String content) {
        logger.info(content, DATE_FORMAT.format(new Date()));
    }

    /**
     * 输出调试信息。
     *
     * @param content 日志内容
     */
    public void debug(String content) {
        logger.debug(content, DATE_FORMAT.format(new Date()));
    }

    /**
     * 输出警告。
     *
     * @param content 日志内容
     */
    public void warn(String content) {
        logger.warn(content, DATE_FORMAT.format(new Date()));
    }

    /**
     * 输出错误。
     *
     * @param content 日志内容
     */
    public void error(String content) {
        logger.error(content, DATE_FORMAT.format(new Date()));
    }

}
