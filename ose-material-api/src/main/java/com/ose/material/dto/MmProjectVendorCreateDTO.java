package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 项目级供货商创建DTO
 */
public class MmProjectVendorCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 4508978295806719723L;

    @Schema(description = "准入状态")
    public List<Long> vendorIds;

    @Schema(description = "公司ID")
    public Long companyId;

    public List<Long> getVendorIds() {
        return vendorIds;
    }

    public void setVendorIds(List<Long> vendorIds) {
        this.vendorIds = vendorIds;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
