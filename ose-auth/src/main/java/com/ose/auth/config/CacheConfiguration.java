package com.ose.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.Serializable;

/**
 * 应用缓存配置。
 */
@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "application.cache")
public class CacheConfiguration implements Serializable {

    private static final long serialVersionUID = 6365823943777696417L;

    // 应用缓存有效时长配置
    private TTL ttl;

    /**
     * 取得应用缓存有效时长配置。
     * @return 应用缓存有效时长配置
     */
    public TTL getTtl() {
        return ttl;
    }

    /**
     * 设置应用缓存有效时长配置。
     * @param ttl 应用缓存有效时长配置
     */
    public void setTtl(TTL ttl) {
        this.ttl = ttl;
    }

    /**
     * 缓存有效时长配置。
     */
    @ConfigurationProperties(prefix = "ttl")
    public static class TTL implements Serializable {


        private static final long serialVersionUID = -1434197884206033168L;

        // 用户代理字符串 ID 在 Redis 缓存中的有效时长
        private int userAgentId;

        // 用户令牌最后刷新时间（Unix 纪元时间，秒）
        private int accessTokenRenewedAt;

        /**
         * 取得用户代理字符串 ID 在 Redis 缓存中的有效时长。
         * @return 用户代理字符串 ID 在 Redis 缓存中的有效时长
         */
        public int getUserAgentId() {
            return userAgentId;
        }

        /**
         * 设置用户代理字符串 ID 在 Redis 缓存中的有效时长。
         * @param userAgentId 用户代理字符串 ID 在 Redis 缓存中的有效时长
         */
        public void setUserAgentId(int userAgentId) {
            this.userAgentId = userAgentId;
        }

        /**
         * 取得用户令牌最后刷新时间。
         * @return 用户令牌最后刷新时间
         */
        public int getAccessTokenRenewedAt() {
            return accessTokenRenewedAt;
        }

        /**
         * 设置用户令牌最后刷新时间。
         * @param accessTokenRenewedAt 用户令牌最后刷新时间
         */
        public void setAccessTokenRenewedAt(int accessTokenRenewedAt) {
            this.accessTokenRenewedAt = accessTokenRenewedAt;
        }

    }

}
