package com.ose.constant;

/**
 * HTTP 请求属性名。
 */
public interface HttpRequestAttributes {

    String CONTEXT = "X-Context";

    String REAL_IP = "X-Real-Ip";

    String FORWARDED_FOR = "X-Forwarded-For";

    String FEIGN_CLIENT = "X-Feign-Client";

}
