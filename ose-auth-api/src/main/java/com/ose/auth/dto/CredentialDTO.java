package com.ose.auth.dto;

import com.ose.annotation.DoNotTrimString;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

/**
 * 用户登录凭证数据传输对象类。
 */
@DoNotTrimString
public class CredentialDTO extends BaseDTO {

    private static final long serialVersionUID = 7459070474435952885L;

    @Schema(description = "登录用户名")
    @NotNull(message = "{error.validation.authentication.username-is-required}")
    private String username;

    @Schema(description = "登录密码")
    @NotNull(message = "{error.validation.authentication.password-is-required}")
    private String password;

    @Schema(description = "是否为sharePoint用户")
    private Boolean isUser = false;

    /**
     * 取得登录用名。
     *
     * @return 登录用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置登录用户名。
     *
     * @param username 登录用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 取得登录密码。
     *
     * @return 登录密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置登录密码。
     *
     * @param password 登录密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getUser() {
        return isUser;
    }

    public void setUser(Boolean user) {
        isUser = user;
    }
}
