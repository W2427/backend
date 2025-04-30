package com.ose.auth.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ose.entity.BaseBizEntity;
import com.ose.util.CryptoUtils;
import com.ose.util.UserAgentUtils;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * 用户访问令牌数据实体类。
 */
@Entity
@Table(
    name = "access_tokens",
    indexes = {
        @Index(
            name = "usrid_ua",
            columnList = "userId,userAgent"
        ),
        @Index(
            name = "usrid_uamd5",
            columnList = "userId,userAgentMd5",
            unique = true
        )
    }
)
public class AccessToken extends BaseBizEntity {

    private static final long serialVersionUID = -4432323233168730255L;

    @Schema(description = "用户 ID")
    @Column(nullable = false)
    private Long userId;

    @Schema(description = "最后更新的远程 IP 地址")
    @Column(nullable = false, length = 15)
    private String remoteAddr;

    @Schema(description = "最后更新的用户代理字符串 ID")
    @Column(nullable = false)
    private Long userAgent;

    @Schema(description = "用户代理字符串（不包含版本信息）MD5 摘要")
    @Column(nullable = false, length = 32)
    private String userAgentMd5;

    @Schema(description = "有效截止时间")
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date validUntil;

    @Schema(description = "有效截止时间（Unix 纪元时间的毫秒）")
    @Column(nullable = false, name = "valid_until_ms")
    private long validUntilMS;

    /**
     * 默认构造方法。
     */
    public AccessToken() {
        super();
        this.setStatus(EntityStatus.ACTIVE);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public Long getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(Long userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserAgentMd5() {
        return userAgentMd5;
    }

    public void setUserAgentMd5(String userAgent) {
        this.userAgentMd5 = getUserAgentMD5(userAgent);
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
        this.setValidUntilMS(validUntil.getTime());
    }

    public long getValidUntilMS() {
        return validUntilMS;
    }

    public void setValidUntilMS(long validUntilMS) {
        this.validUntilMS = validUntilMS;
    }

    public static String getUserAgentMD5(String userAgent) {
        return CryptoUtils.md5(
            UserAgentUtils.wipeVersionInfo(userAgent)
        ).toUpperCase();
    }

}
