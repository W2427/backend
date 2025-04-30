package com.ose.auth.domain.model.service;

import com.ose.auth.config.CacheConfiguration;
import com.ose.auth.domain.model.repository.UserAgentRepository;
import com.ose.auth.entity.UserAgent;
import com.ose.exception.AccessDeniedError;
import com.ose.service.StringRedisService;
import com.ose.util.CryptoUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 用户代理字符串服务。
 */
@Component
@EnableConfigurationProperties(CacheConfiguration.class)
public class UserAgentService extends StringRedisService implements UserAgentInterface {

    // 用户代理字符串 ID 在 Redis 中的缓存时长
    private final int userAgentIdRedisKeyTTL;

    // 用户代理字符串操作仓库
    private final UserAgentRepository userAgentRepository;

    /**
     * 取得 Redis 中用户代理字符串 ID 的 KEY。
     *
     * @param userAgent 用户代理字符串
     * @return Redis 中用户代理字符串 ID 的 KEY
     */
    private static String getUserAgentIdRedisKey(String userAgent) {

        if (StringUtils.isEmpty(userAgent, true)) {
            throw new AccessDeniedError();
        }

        return "USER-AGENT-ID:" + CryptoUtils.md5(userAgent).toUpperCase();
    }

    /**
     * 构造方法。
     *
     * @param stringRedisTemplate Redis 模板
     * @param userAgentRepository 用户代理字符串操作仓库
     */
    @Autowired
    public UserAgentService(
        CacheConfiguration cacheConfiguration,
        StringRedisTemplate stringRedisTemplate,
        UserAgentRepository userAgentRepository
    ) {
        super(stringRedisTemplate);

        this.userAgentIdRedisKeyTTL = cacheConfiguration
            .getTtl()
            .getUserAgentId();

        this.userAgentRepository = userAgentRepository;
    }

    /**
     * 取得用户代理字符串 ID，若为未记录的用户代理字符串则新建记录。
     *
     * @param userAgent 用户代理字符串
     * @return 用户代理字符串 ID
     */
    @Override
    public Long fetchId(final String userAgent) {

        String userAgentIdRedisKey = getUserAgentIdRedisKey(userAgent);
        Long userAgentId = LongUtils.parseLong(getRedisKey(userAgentIdRedisKey, userAgentIdRedisKeyTTL));

        if (LongUtils.isEmpty(userAgentId)) {

            UserAgent userAgentEntity
                = userAgentRepository.findByUserAgent(userAgent);

            if (userAgentEntity == null) {
                userAgentEntity = new UserAgent(userAgent);
                userAgentRepository.save(userAgentEntity);
            }

            userAgentId = userAgentEntity.getId();

            setRedisKey(userAgentIdRedisKey, userAgentId.toString());
        }

        return userAgentId;
    }

}
