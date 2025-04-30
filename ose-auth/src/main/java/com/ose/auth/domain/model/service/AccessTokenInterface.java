package com.ose.auth.domain.model.service;

import com.ose.auth.entity.UserProfile;
import com.ose.dto.OperatorDTO;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户访问令牌接口。
 */
public interface AccessTokenInterface {

    /**
     * 创建用户访问令牌。
     *
     * @param userProfile 用户实体数据
     * @param remoteAddr  远程 IP 地址
     * @param userAgent   用户代理字符串
     * @param userAgentId 用户代理字符串 ID
     * @return 用户登录授权数据
     */
    String create(UserProfile userProfile, String remoteAddr, String userAgent, Long userAgentId);

    /**
     * 从 HTTP 请求头中读取访问令牌。
     *
     * @param request HTTP 请求实例
     * @return 访问令牌
     */
    String getAccessTokenFromRequestHeader(HttpServletRequest request);

    /**
     * 校验用户访问令牌。
     *
     * @param remoteAddr  远程 IP 地址
     * @param userAgent   用户代理字符串
     * @param userAgentId 用户代理字符串 ID
     * @param accessToken 用户访问令牌
     * @return 用户信息
     */
    OperatorDTO claim(String remoteAddr, String userAgent, Long userAgentId, String accessToken);

    /**
     * 销毁用户令牌。
     *
     * @param accessTokenId 用户访问令牌 ID
     */
    void destroy(Long accessTokenId);

}
