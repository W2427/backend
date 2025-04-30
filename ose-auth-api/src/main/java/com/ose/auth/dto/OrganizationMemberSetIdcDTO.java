package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class OrganizationMemberSetIdcDTO extends BaseDTO {

    private static final long serialVersionUID = -1997472037684831925L;
    @Schema(description = "是否为IDC负责人")
    private Boolean idc;

    public Boolean getIdc() {
        return idc;
    }

    public void setIdc(Boolean idc) {
        this.idc = idc;
    }
}
