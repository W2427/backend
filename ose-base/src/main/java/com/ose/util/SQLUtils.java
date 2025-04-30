package com.ose.util;

/**
 * SQL 处理工具。
 */
public class SQLUtils {

    /**
     * 转义 LIKE 值中的 \、%、_、[ 等字符。
     *
     * @param like LIKE 值
     * @return 转义后的字符串
     */
    public static String escapeLike(String like) {

        if (StringUtils.isEmpty(like, true)) {
            return like;
        }

        return like
            .replaceAll("\\\\", "\\\\")
            .replaceAll("%", "\\\\%")
            .replaceAll("_", "\\\\_")
            .replaceAll("\\[", "\\\\[");
    }

}
