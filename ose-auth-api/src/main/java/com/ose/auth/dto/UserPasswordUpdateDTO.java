package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户密码重置表单。
 */
public class UserPasswordUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = -2765353958521814024L;

    @Schema(description = "原密码")
    private String originalPassword;

    @Schema(description = "新密码")
    private String password;

    public String getOriginalPassword() {
        return originalPassword;
    }

    public void setOriginalPassword(String originalPassword) {
        this.originalPassword = originalPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
