package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class RoleMembersDTO extends BaseDTO {

    private static final long serialVersionUID = 3157818929216853813L;

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "用户手机号")
    private String mobile;

    @Schema(description = "角色")
    private List<String> roles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
