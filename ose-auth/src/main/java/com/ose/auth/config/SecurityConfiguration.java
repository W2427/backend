package com.ose.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.Serializable;

/**
 * 应用安全配置。
 */
@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "application.security")
public class SecurityConfiguration implements Serializable {

    private static final long serialVersionUID = -3562686599314084257L;

    // 用户访问令牌 KEY
    private String accessTokenKey;

    // 用户访问令牌 TTL（秒）
    private long accessTokenTtl;

    // 用户访问令牌刷新频率（秒）
    private long accessTokenRenewFrequency;

    // 图形验证码 TTL（秒）
    private long captchaTtl;

    // 电子邮件验证码 TTL（秒）
    private long emailVerificationTtl;

    // 短信验证码 TTL（秒）
    private long smsVerificationTtl;

    /**
     * 取得用户访问令牌 KEY。
     * @return 用户访问令牌 KEY
     */
    public String getAccessTokenKey() {
        return accessTokenKey;
    }

    /**
     * 设置用户访问令牌 KEY。
     * @param accessTokenKey 用户访问令牌 KEY
     */
    public void setAccessTokenKey(String accessTokenKey) {
        this.accessTokenKey = accessTokenKey;
    }

    /**
     * 取得用户访问令牌 TTL（秒）。
     * @return 用户访问令牌 TTL（秒）
     */
    public long getAccessTokenTtl() {
        return accessTokenTtl;
    }

    /**
     * 取得用户访问令牌 TTL（毫秒）。
     * @return 用户访问令牌 TTL（毫秒）
     */
    public long getAccessTokenTtlMS() {
        return accessTokenTtl * 1000;
    }

    /**
     * 设置用户访问令牌 TTL。
     * @param accessTokenTtl 用户访问令牌 TTL
     */
    public void setAccessTokenTtl(long accessTokenTtl) {
        this.accessTokenTtl = accessTokenTtl;
    }

    /**
     * 取得用户访问令牌刷新频率（秒）。
     * @return 用户访问令牌刷新频率（秒）
     */
    public long getAccessTokenRenewFrequency() {
        return accessTokenRenewFrequency;
    }

    /**
     * 取得用户访问令牌刷新频率（毫秒）。
     * @return 用户访问令牌刷新频率（毫秒）
     */
    public long getAccessTokenRenewFrequencyMS() {
        return accessTokenRenewFrequency * 1000;
    }

    /**
     * 设置用户访问令牌刷新频率（秒）。
     * @param accessTokenRenewFrequency 用户访问令牌刷新频率（秒）
     */
    public void setAccessTokenRenewFrequency(long accessTokenRenewFrequency) {
        this.accessTokenRenewFrequency = accessTokenRenewFrequency;
    }

    /**
     * 取得图形验证码 TTL（秒）。
     * @return 图形验证码 TTL（秒）
     */
    public long getCaptchaTtl() {
        return captchaTtl;
    }

    /**
     * 取得图形验证码 TTL（毫秒）。
     * @return 图形验证码 TTL（毫秒）
     */
    public long getCaptchaTtlMS() {
        return captchaTtl * 1000;
    }

    /**
     * 设置图形验证码 TTL（秒）。
     * @param captchaTtl 图形验证码 TTL（秒）
     */
    public void setCaptchaTtl(long captchaTtl) {
        this.captchaTtl = captchaTtl;
    }

    /**
     * 取得电子邮件验证码 TTL（秒）。
     * @return 电子邮件验证码 TTL（秒）
     */
    public long getEmailVerificationTtl() {
        return emailVerificationTtl;
    }

    /**
     * 取得电子邮件验证码 TTL（毫秒）。
     * @return 电子邮件验证码 TTL（毫秒）
     */
    public long getEmailVerificationTtlMS() {
        return emailVerificationTtl * 1000;
    }

    /**
     * 设置电子邮件验证码 TTL（秒）。
     * @param emailVerificationTtl 电子邮件验证码 TTL（秒）
     */
    public void setEmailVerificationTtl(long emailVerificationTtl) {
        this.emailVerificationTtl = emailVerificationTtl;
    }

    /**
     * 取得短信验证码 TTL（秒）。
     * @return 短信验证码 TTL（秒）
     */
    public long getSmsVerificationTtl() {
        return smsVerificationTtl;
    }

    /**
     * 取得短信验证码 TTL（毫秒）。
     * @return 短信验证码 TTL（毫秒）
     */
    public long getSmsVerificationTtlMS() {
        return smsVerificationTtl * 1000;
    }

    /**
     * 设置短信验证码 TTL（秒）。
     * @param smsVerificationTtl 短信验证码 TTL（秒）
     */
    public void setSmsVerificationTtl(long smsVerificationTtl) {
        this.smsVerificationTtl = smsVerificationTtl;
    }

}
