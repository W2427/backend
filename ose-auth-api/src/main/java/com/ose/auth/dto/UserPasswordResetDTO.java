package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户密码重置表单。
 */
public class UserPasswordResetDTO extends BaseDTO {

    private static final long serialVersionUID = 6662036378792319006L;

    @Schema(description = "电子邮件验证码或短信验证码")
    private String verificationCode;

    @Schema(description = "新密码")
    private String password;

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
