package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class SwitchOrgDTO extends BaseDTO {

    private static final long serialVersionUID = -213143389749592575L;

    @Schema(description = "项目组织ID")
    private Long organizationId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
