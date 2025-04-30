package com.ose.auth.domain.model.service;

import com.ose.util.*;
import com.ose.auth.config.CacheConfiguration;
import com.ose.auth.config.SecurityConfiguration;
import com.ose.auth.domain.model.repository.AccessTokenRepository;
import com.ose.auth.domain.model.repository.UserProfileRepository;
import com.ose.auth.dto.AccessTokenDTO;
import com.ose.auth.entity.AccessToken;
import com.ose.auth.entity.UserProfile;
import com.ose.dto.OperatorDTO;
import com.ose.exception.AccessTokenExpiredError;
import com.ose.exception.AccessTokenInvalidError;
import com.ose.exception.UnauthorizedError;
import com.ose.service.StringRedisService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

/**
 * 用户访问令牌服务。
 */
@Component
@EnableConfigurationProperties(SecurityConfiguration.class)
public class AccessTokenService extends StringRedisService implements AccessTokenInterface {

    // 用户访问令牌 KEY
    private final String accessTokenSecretKey;

    // 用户访问令牌有效期长度（毫秒）
    private final long accessTokenTTL;

    // 用户访问令牌刷新频率（毫秒）
    private final long accessTokenRenewFrequency;

    // 用户访问令牌所有者信息左后更新时间缓存时长
    private final int accessTokenRenewedAtTTL;

    // 用户访问令牌操作库
    private final AccessTokenRepository accessTokenRepository;

    // 用户资料操作仓库
    private final UserProfileRepository userProfileRepository;

    /**
     * 取得 Redis 中访问令牌最后更新时间的 KEY。
     *
     * @param accessTokenId 访问令牌 ID
     * @return Redis 中访问令牌最后更新时间的 KEY
     */
    private static String getAccessTokenLastModifiedAtKey(Long accessTokenId) {
        return "ACCESS-TOKEN:" + accessTokenId + ":LAST-MODIFIED-AT";
    }

    /**
     * 构造方法。
     *
     * @param securityConfiguration 安全配置
     * @param cacheConfiguration    缓存配置
     * @param stringRedisTemplate   Redis 模板
     * @param accessTokenRepository 用户访问令牌操作仓库
     * @param userProfileRepository 用户资料操作仓库
     */
    @Autowired
    public AccessTokenService(
        SecurityConfiguration securityConfiguration,
        CacheConfiguration cacheConfiguration,
        StringRedisTemplate stringRedisTemplate,
        AccessTokenRepository accessTokenRepository,
        UserProfileRepository userProfileRepository
    ) {
        super(stringRedisTemplate);

        this.accessTokenSecretKey = securityConfiguration.getAccessTokenKey();

        this.accessTokenTTL = securityConfiguration.getAccessTokenTtlMS();

        this.accessTokenRenewFrequency
            = securityConfiguration.getAccessTokenRenewFrequencyMS();

        this.accessTokenRenewedAtTTL
            = cacheConfiguration.getTtl().getAccessTokenRenewedAt();

        this.accessTokenRepository = accessTokenRepository;

        this.userProfileRepository = userProfileRepository;
    }

    /**
     * 创建用户访问令牌。
     *
     * @param userProfile 用户实体数据
     * @param remoteAddr  远程 IP 地址
     * @param userAgent   用户代理字符串
     * @param userAgentId 用户代理字符串 ID
     * @return 用户登录授权数据
     */
    @Override
    public String create(
        final UserProfile userProfile,
        final String remoteAddr,
        final String userAgent,
        final Long userAgentId
    ) {

        final long now = System.currentTimeMillis();

        // 获取已生成的用户访问令牌
        AccessToken accessToken = accessTokenRepository
            .findByUserIdAndUserAgentMd5(
                userProfile.getId(),
                AccessToken.getUserAgentMD5(userAgent)
            );

        // 若取得的用户访问令牌已过期则将其销毁
        if (accessToken != null
            && now > accessToken.getValidUntilMS()) {
            destroy(accessToken.getId());
            accessToken = null;
        }

        // 若尚未创建过用户访问令牌则生成新的用户访问令牌
        if (accessToken == null) {
            accessToken = new AccessToken();
            accessToken.setUserId(userProfile.getId());
            accessToken.setRemoteAddr(RemoteAddressUtils.pad(remoteAddr));
            accessToken.setUserAgent(userAgentId);
            accessToken.setUserAgentMd5(userAgent);
            accessToken.setValidUntil(new Date(now + this.accessTokenTTL));
            accessTokenRepository.save(accessToken);
        }

        // 构造访问令牌数据对象
        final AccessTokenDTO accessTokenDTO = new AccessTokenDTO(
            accessToken.getId(),
            userProfile.getId(),
            remoteAddr,
            userAgentId
        );

        BeanUtils.copyProperties(userProfile, accessTokenDTO);

        // 生成用户访问令牌
        return Jwts
            .builder()
            .setSubject(accessTokenDTO.toString())
            .signWith(
                SignatureAlgorithm.HS384,
                this.accessTokenSecretKey
                    + UserAgentUtils.wipeVersionInfo(userAgent)
            )
            .compact();
    }

    /**
     * 从 HTTP 请求头中读取访问令牌。
     *
     * @param request HTTP 请求实例
     * @return 访问令牌
     */
    @Override
    public String getAccessTokenFromRequestHeader(
        final HttpServletRequest request
    ) {

        final String authorization
            = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null) {
            throw new UnauthorizedError();
        }

        if (!authorization.startsWith("Bearer ")) {
            throw new AccessTokenInvalidError();
        }

        return authorization.substring(7);
    }

    /**
     * 刷新用户令牌。
     *
     * @param remoteAddr     远程 IP 地址
     * @param userAgent      用户代理字符串
     * @param userAgentId    用户代理字符串 ID
     * @param accessTokenDTO 用户访问令牌数据
     * @return 更新后的访问令牌
     */
    private String renew(
        final String remoteAddr,
        final String userAgent,
        final Long userAgentId,
        final AccessTokenDTO accessTokenDTO
    ) {

        final Long accessTokenId = accessTokenDTO.getId();

        final String accessTokenLastModifiedAtKey
            = getAccessTokenLastModifiedAtKey(accessTokenId);

        // 取得缓存的令牌所有者信息的最后更新时间
        final String lastModifyAt = getRedisKey(
            getAccessTokenLastModifiedAtKey(accessTokenId)
        );

        // 若缓存中存在令牌所有者信息最后更新时间且当前用户令牌为最新则继续
        if (!StringUtils.isEmpty(lastModifyAt)
            && LongUtils.parseLong(lastModifyAt)
            == accessTokenDTO.getLastModifiedAt().getTime()) {
            return null;
        }

        // 尝试从数据库中取得访问令牌信息
        final AccessToken accessToken = accessTokenRepository
            .findByUserIdAndUserAgentMd5(
                accessTokenDTO.getUserId(),
                AccessToken.getUserAgentMD5(userAgent)
            );

        // 若令牌不存在则返回无效错误
        if (accessToken == null) {
            throw new AccessTokenInvalidError();
        }

        final long currentTime = System.currentTimeMillis();
        final long validUntil = accessToken.getValidUntilMS();
        boolean renewed = false;

        // 若令牌已过期则返回错误
        if (currentTime > validUntil) {
            accessTokenRepository.deleteById(accessToken.getId());
            throw new AccessTokenExpiredError();
        }

        // 刷新访问令牌
        if (accessTokenTTL - (validUntil - currentTime)
            > accessTokenRenewFrequency
            || !remoteAddr.equals(accessToken.getRemoteAddr())
            || !userAgentId.equals(accessToken.getUserAgent())) {
            accessToken.setRemoteAddr(remoteAddr);
            accessToken.setUserAgent(userAgentId);
            accessToken.setValidUntil(new Date(currentTime + accessTokenTTL));
            accessTokenRepository.save(accessToken);
            renewed = true;
        }

        // 设置用户令牌所有者信息的最后更新时间
        setRedisKey(
            accessTokenLastModifiedAtKey,
            Long.toString(accessToken.getLastModifiedAt().getTime()),
            accessTokenRenewedAtTTL
        );

        if (!renewed) {
            return null;
        }

        // 取得用户信息
        Optional<UserProfile> userResult
            = userProfileRepository.findById(accessToken.getUserId());

        if (!userResult.isPresent()) {
            throw new AccessTokenInvalidError();
        }

        // 重新生成访问令牌
        return create(
            userResult.get(),
            remoteAddr,
            userAgent,
            userAgentId
        );

    }

    /**
     * 校验用户访问令牌。
     *
     * @param remoteAddr  远程 IP 地址
     * @param userAgent   用户代理字符串
     * @param userAgentId 用户代理字符串 ID
     * @param accessToken 用户访问令牌
     * @return 用户信息
     */
    @Override
    public OperatorDTO claim(
        final String remoteAddr,
        final String userAgent,
        final Long userAgentId,
        final String accessToken
    ) {

        try {

            // 校验 JWT，取得用户信息
            AccessTokenDTO accessTokenDTO = new AccessTokenDTO(
                Jwts
                    .parser()
                    .setSigningKey(
                        this.accessTokenSecretKey
                            + UserAgentUtils.wipeVersionInfo(userAgent)
                    ).build()
                    .parseClaimsJws(accessToken)
                    .getBody()
                    .getSubject()
            );

            // 刷新访问令牌
            String renewedAccessToken
                = renew(remoteAddr, userAgent, userAgentId, accessTokenDTO);

            // 从令牌中读取所有者用户信息
            return BeanUtils.copyProperties(
                accessTokenDTO,
                (new OperatorDTO(accessTokenDTO.getUserId()))
                    .setAccessTokenId(accessTokenDTO.getId())
                    .setRenewedAccessToken(renewedAccessToken)
            );

        } catch (final ExpiredJwtException e) {
            throw new AccessTokenExpiredError();
        } catch (final JwtException e) {
            throw new AccessTokenInvalidError();
        }

    }

    /**
     * 销毁用户令牌。
     *
     * @param accessTokenId 用户访问令牌 ID
     */
    @Override
    public void destroy(Long accessTokenId) {
        accessTokenRepository.deleteById(accessTokenId);
        deleteRedisKey(getAccessTokenLastModifiedAtKey(accessTokenId));
    }

}
