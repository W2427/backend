package com.ose.util;

import com.ose.constant.HttpRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 远程地址工具。
 */
public class RemoteAddressUtils {

    // 当前服务器信息
    private static InetAddress localhost;

    static {
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            localhost = null;
        }
    }

    // IP v4 十进制形式正则表达式
    private static final Pattern IP_V4_DEC_PATTERN = Pattern.compile(
        "^([0-9]+)\\.([0-9]+)\\.([0-9]+)\\.([0-9]+)$"
    );

    // IP v4 十六进制形式正则表达式
    private static final Pattern IP_V4_HEX_PATTERN = Pattern.compile(
        "^([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})$",
        Pattern.CASE_INSENSITIVE
    );

    /**
     * 取得当前服务器主机名。
     *
     * @return 主机名
     */
    public static String getHostname() {
        return localhost == null ? "" : localhost.getHostName();
    }

    /**
     * 取得当前服务器内网 IP 地址。
     *
     * @return 内网 IP 地址
     */
    public static String getHostAddress() {
        return localhost == null ? "" : localhost.getHostAddress();
    }

    /**
     * 取得当前服务器标签。
     *
     * @return 服务器标签
     */
    public static String getHostLabel() {
        return localhost == null
            ? ""
            : String.format("%s (%s)", localhost.getHostName(), localhost.getHostAddress());
    }

    /**
     * 取得客户端远程 IP 地址。
     *
     * @param request HTTP 请求
     * @return 客户端远程 IP 地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {

        String remoteAddr;

        if (!StringUtils.isEmpty(remoteAddr = request.getHeader(HttpRequestAttributes.REAL_IP))) {
            return remoteAddr;
        }

        if (!StringUtils.isEmpty(remoteAddr = request.getHeader(HttpRequestAttributes.FORWARDED_FOR))) {
            return remoteAddr.split(",")[0];
        }

        return request.getRemoteAddr();
    }

    /**
     * 检查字符串是否为 IP v4。
     *
     * @param ipv4 输入 IP v4 字符串
     * @return 检查结果
     */
    public static boolean isIPv4(String ipv4) {

        if (ipv4 == null || !IP_V4_DEC_PATTERN.matcher(ipv4).matches()) {
            return false;
        }

        for (String segment : ipv4.split("\\.")) {
            if (Integer.parseInt(segment) > 255) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查 IP v4 地址是否为内网地址。
     *
     * @param ipv4 输入 IP v4 字符串
     * @return 检查结果
     */
    public static boolean isPrivate(String ipv4) {

        if (!isIPv4(ipv4)) {
            return false;
        }

        String[] segments = ipv4.split("\\.");

        int[] numbers = new int[4];

        for (int i = 0; i < segments.length; i++) {
            numbers[i] = Integer.parseInt(segments[i]);
            segments[i] = "" + numbers[i];
        }

        return numbers[0] == 10
            || (numbers[0] == 172 && numbers[1] >= 16 && numbers[1] <= 31)
            || (numbers[0] == 192 && numbers[1] == 168)
            || "127.0.0.1".equals(String.join(".", segments));
    }

    /**
     * 检查 IP v4 地址是否为环回地址。
     *
     * @param ipv4 输入 IP v4 字符串
     * @return 检查结果
     */
    public static boolean isLoopback(String ipv4) {

        if (!isIPv4(ipv4)) {
            return false;
        }

        return "127.0.0.1".equals(ipv4);
    }

    /**
     * 将 IP v4 地址转为十六进制形式。
     *
     * @param ipv4 IP v4 地址
     * @return IP v4 地址转为十六进制形式
     */
    public static String toHex(String ipv4) {

        String[] segments = ipv4.split("\\.");

        StringBuilder hex = new StringBuilder();

        for (String segment : segments) {

            segment = Integer.toString(Integer.parseInt(segment, 10), 16);

            if (segment.length() == 1) {
                hex.append("0");
            }

            hex.append(segment);
        }

        return hex.toString().toUpperCase();
    }

    /**
     * 将 IP v4 由十六进制形式转为十进制形式。
     *
     * @param hex IP v4 地址的十六进制形式
     * @return IP v4 地址
     */
    public static String fromHex(String hex) {

        Matcher m = IP_V4_HEX_PATTERN.matcher(hex);

        if (!m.matches()) {
            return null;
        }

        return String.join(
            ".",
            new String[]{
                "" + Integer.parseInt(m.group(1), 16),
                "" + Integer.parseInt(m.group(2), 16),
                "" + Integer.parseInt(m.group(3), 16),
                "" + Integer.parseInt(m.group(4), 16)
            }
        );

    }

    /**
     * 填充 IP v4 地址。
     *
     * @param ipv4 IP v4 地址
     * @return 填充后的 IP v4 地址
     */
    public static String pad(String ipv4) {

        String[] segments = ipv4.split("\\.");

        for (int i = 0; i < segments.length; i++) {
            segments[i] = StringUtils.last(
                "00" + Integer.parseInt(segments[i], 10),
                3
            );
        }

        return String.join(".", segments);
    }

}
