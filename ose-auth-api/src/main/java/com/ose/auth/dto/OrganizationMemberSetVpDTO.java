package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class OrganizationMemberSetVpDTO extends BaseDTO {

    private static final long serialVersionUID = -1729709296568983343L;
    @Schema(description = "用户 ID")
    private Boolean applyRole;

    public Boolean getApplyRole() {
        return applyRole;
    }

    public void setApplyRole(Boolean applyRole) {
        this.applyRole = applyRole;
    }
}
