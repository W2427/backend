package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.material.vo.MaterialOrganizationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

/**
 * 仓库
 */
@Entity
@Table(name = "mm_material_code_type")
public class MmMaterialCodeTypeEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 5124872224099539746L;

    @Schema(description ="组织ID")
    @Column
    public Long orgId;

    @Schema(description ="项目ID")
    @Column
    public Long projectId;

    @Schema(description ="公司ID")
    @Column
    public Long companyId;

    @Schema(description ="流水号")
    private Integer seqNumber;

    @Schema(description ="仓库名")
    @Column
    public String name;

    @Schema(description ="仓库地址")
    @Column
    public String address;

    @Schema(description ="仓库类型（公司、项目）")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    public MaterialOrganizationType materialOrganizationType;

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

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MaterialOrganizationType getWareHouseType() {
        return materialOrganizationType;
    }

    public void setWareHouseType(MaterialOrganizationType materialOrganizationType) {
        this.materialOrganizationType = materialOrganizationType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
