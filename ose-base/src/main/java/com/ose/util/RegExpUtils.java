package com.ose.util;

import java.util.regex.Pattern;

/**
 * 正则表达式工具。
 */
public class RegExpUtils {

    // 数据实体 ID 格式
    public static final String ID = "^[0-9A-Z]{16}$";
    public static final String DEC_ID = "^[0-9]{18,20}$";
    public static final String GUID = "^[0-9a-fA-F]+(-[0-9a-fA-F]+)+$";

    // 登录用户名格式
//    public static final String USERNAME = "^[a-z](_?[0-9a-z]{2,}){1,31}$";
    public static final String USERNAME = "^(?=.*[a-zA-Z])[a-zA-Z0-9_]+$";
    // 电子邮箱地址格式
    public static final String EMAIL = "^[0-9a-zA-Z]+([._%+\\-][0-9a-zA-Z]+)*@[0-9a-zA-Z]+([.\\-][0-9a-zA-Z]+)*\\.[a-zA-Z]{2,6}$";

    // 手机号码格式
    public static final String MOBILE = "^1[345789][0-9]{9}$";
    public static final String MOBILE_WITH_REGION = "^(\\+?0?86)?1[3578][0-9]{9}$";

    // 权限值
    public static final String PRIVILEGE = "^([/-][0-9A-Za-z]+)*$";

    /**
     * 检查输入的值是否符合数据实体 ID 格式。
     *
     * @param string 输入值
     * @return 是否符合数据实体 ID 格式
     */
    public static boolean isID(String string) {
        return string != null && Pattern.matches(ID, string);
    }


    /**
     * 检查输入的值是否符合数据实体 ID 格式。
     *
     * @param string 输入值
     * @return 是否符合数据实体 ID 格式
     */
    public static boolean isDecID(String string) {
        return string != null && Pattern.matches(DEC_ID, string);
    }

    /**
     * 检查输入的值是否符合 GUID 格式。
     *
     * @param string 输入值
     * @return 是否符合 GUID 格式
     */
    public static boolean isGUID(String string) {
        return string != null && Pattern.matches(GUID, string);
    }

    /**
     * 检查输入的值是否符合登录用户名格式。
     *
     * @param string 输入值
     * @return 是否符合登录用户名格式
     */
    public static boolean isUsername(String string) {
        return string != null && Pattern.matches(USERNAME, string);
    }

    /**
     * 检查输入的值是否符合电子邮箱地址格式。
     *
     * @param string 输入值
     * @return 是否符合电子邮箱地址格式
     */
    public static boolean isEmailAddress(String string) {
        return string != null && Pattern.matches(EMAIL, string);
    }

    /**
     * 检查输入的值是否符合手机号码格式。
     *
     * @param string 输入值
     * @return 是否符合手机号码格式
     */
    public static boolean isMobileNumber(String string) {
        return string != null && Pattern.matches(MOBILE_WITH_REGION, string);
    }

    /**
     * 检查输入的值是否符合权限格式。
     *
     * @param string 输入值
     * @return 是否符合权限格式
     */
    public static boolean isPrivilege(String string) {
        return string != null
            && (Pattern.matches(PRIVILEGE, string)
            || "none".equals(string)
            || "all".equals(string)
            || "department".equals(string)
            || "role".equals(string)
            || "member".equals(string)
            || "document".equals(string)
            || "project".equals(string)
        );
    }

}
