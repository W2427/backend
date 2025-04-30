package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class UserRoleDTO extends BaseDTO {

    private static final long serialVersionUID = -8132302145777439362L;

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "成员ID列表")
    private Long[] members;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long[] getMembers() {
        return members;
    }

    public void setMembers(Long[] members) {
        this.members = members;
    }
}
