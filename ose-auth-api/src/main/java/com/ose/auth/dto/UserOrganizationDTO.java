package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class UserOrganizationDTO extends BaseDTO {

    private static final long serialVersionUID = -8209855384905232048L;

    @Schema(description = "组织 ID")
    private Long organizationId;

    @Schema(description = "成员用户 ID 数组")
    private Long[] members;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long[] getMembers() {
        return members;
    }

    public void setMembers(Long[] members) {
        this.members = members;
    }
}
