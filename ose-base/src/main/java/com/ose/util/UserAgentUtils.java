package com.ose.util;

/**
 * 用户代理字符串工具。
 */
public class UserAgentUtils {

    /**
     * 去除用户代理字符串中的版本信息。
     *
     * @param userAgent 用户代理字符串
     * @return 去除版本信息后的用户代理字符串
     */
    public static String wipeVersionInfo(String userAgent) {
        return userAgent
            .replaceAll("\\stoken/\\S+", "")
            .replaceAll("/\\S*(\\s|$)", "")
            .replaceAll("[_\\W\\d]+", "");
    }

}
