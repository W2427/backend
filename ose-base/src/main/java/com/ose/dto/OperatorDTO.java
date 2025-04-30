package com.ose.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 操作者信息。
 */
public class OperatorDTO extends BaseDTO {

    private static final long serialVersionUID = -4168966237747607187L;

    @Schema(description = "用户 ID")
    private Long id;

    @Schema(description = "用户类型")
    private String type;

    @Schema(description = "头像地址")
    private String logo;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "访问令牌 ID")
    private Long accessTokenId;

    @Schema(description = "客户端远程 IP 地址")
    private String remoteAddr;

    @Schema(description = "用户代理字符串 ID")
    private Long userAgentId;

    @Schema(description = "刷新后的访问令牌")
    private String renewedAccessToken;

    public OperatorDTO() {
    }

    public OperatorDTO(Long id) {
        this.id = id;
    }

    public OperatorDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public OperatorDTO setType(String type) {
        this.type = type;
        return this;
    }

    public String getLogo() {
        return logo;
    }

    public OperatorDTO setLogo(String logo) {
        this.logo = logo;
        return this;
    }

    public String getName() {
        return name;
    }

    public OperatorDTO setName(String name) {
        this.name = name;
        return this;
    }

    @JsonIgnore
    public Long getAccessTokenId() {
        return accessTokenId;
    }

    public OperatorDTO setAccessTokenId(Long accessTokenId) {
        this.accessTokenId = accessTokenId;
        return this;
    }

    @JsonIgnore
    public String getRemoteAddr() {
        return remoteAddr;
    }

    public OperatorDTO setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
        return this;
    }

    @JsonIgnore
    public Long getUserAgentId() {
        return userAgentId;
    }

    public OperatorDTO setUserAgentId(Long userAgentId) {
        this.userAgentId = userAgentId;
        return this;
    }

    @JsonIgnore
    public String getRenewedAccessToken() {
        return renewedAccessToken;
    }

    public OperatorDTO setRenewedAccessToken(String renewedAccessToken) {
        this.renewedAccessToken = renewedAccessToken;
        return this;
    }

}
