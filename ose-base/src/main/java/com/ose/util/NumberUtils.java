package com.ose.util;

/**
 * 数值工具。
 */
public class NumberUtils {

    public static Integer toInteger(Number number) {

        if (number == null) {
            return null;
        }

        return number.intValue();
    }

}
