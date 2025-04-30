package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.material.vo.MaterialOrganizationType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * BOM创建DTO
 */
public class MmMaterialCodeTypeCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -4816877115374640158L;

    @Schema(description ="组织ID")
    public Long orgId;

    @Schema(description ="项目ID")
    public Long projectId;

    @Schema(description ="公司ID")
    public Long companyId;

    @Schema(description ="仓库类型")
    public MaterialOrganizationType materialOrganizationType;

    @Schema(description ="仓库名")
    private String name;

    @Schema(description ="流水号")
    private Integer seqNumber;

    @Schema(description ="仓库地址")
    private String address;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public MaterialOrganizationType getWareHouseType() {
        return materialOrganizationType;
    }

    public void setWareHouseType(MaterialOrganizationType materialOrganizationType) {
        this.materialOrganizationType = materialOrganizationType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
