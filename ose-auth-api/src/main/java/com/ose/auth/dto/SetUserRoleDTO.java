package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class SetUserRoleDTO extends BaseDTO {

    private static final long serialVersionUID = 1964177401541144974L;

    @Schema(description = "角色ID列表")
    List<Long> roleIds;

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
