package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 权限检查数据传输对象。
 */
public class AuthCheckDTO extends BaseDTO {


    private static final long serialVersionUID = -5094987354155956896L;

    @Schema(description = "授权类型，默认为【Bearer】")
    private String authorization;

    @Schema(description = "组织 ID")
    private String userAgent;

    private String remoteAddr;

    private Boolean isAuth;

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public Boolean getAuth() {
        return isAuth;
    }

    public void setAuth(Boolean auth) {
        isAuth = auth;
    }
}
