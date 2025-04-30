package com.ose.auth.domain.model.service;

import jakarta.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpHeaders.USER_AGENT;

/**
 * 用户代理字符串接口。
 */
public interface UserAgentInterface {

    /**
     * 取得用户代理字符串 ID，若为未记录的用户代理字符串则新建记录。
     *
     * @param userAgent 用户代理字符串
     * @return 用户代理字符串 ID
     */
    Long fetchId(String userAgent);

    /**
     * 取得用户代理字符串 ID，若为未记录的用户代理字符串则新建记录。
     *
     * @param request HTTP 请求
     * @return 用户代理字符串 ID
     */
    default Long fetchId(HttpServletRequest request) {
        return fetchId(request.getHeader(USER_AGENT));
    }

}
