package com.ose.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信工具。
 */
public class SMSUtils {

    private static String sendURI;
    private static String signature;
    private static String account;
    private static String password;

    /**
     * 初始化。
     *
     * @param sendURI  发送接口 URI
     * @param account  用户名
     * @param password 密码
     */
    public static void init(
        String sendURI,
        String signature,
        String account,
        String password
    ) {
        SMSUtils.sendURI = sendURI;
        SMSUtils.signature = signature;
        SMSUtils.account = account;
        SMSUtils.password = password;
    }

    /**
     * 发送短信息。
     *
     * @param to   发送目标手机号码
     * @param text 短信内容
     */
    public static void send(String to, String text) {
        send(new String[]{to}, text);
    }

    /**
     * 发送短信息。
     * 文档 https://www.showdoc.cc/web/#/1619992?page_id=14891467
     *
     * @param to   发送目标手机号码
     * @param text 短信内容
     */
    public static void send(String[] to, String text) {

        Map<String, String> body = new HashMap<>();

        body.put("account", account);
        body.put("password", password);
        body.put("msg", "【" + signature + "】" + text);
        body.put("phone", String.join(",", to));

        HttpUtils.postJSON(sendURI, body, String.class);
    }

}
