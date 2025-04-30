package com.ose.tasks.entity;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "subcons")
public class Subcon extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 3472976134839430728L;

    @Schema(description = "组织ID")
    @Column
    private Long orgId;

    @Schema(description = "项目ID")
    @Column
    private Long projectId;

    @Schema(description = "分包商名称")
    @Column
    private String name;

    @Schema(description = "分包商全称")
    @Column
    private String fullName;

    @Schema(description = "分包商地址")
    @Column
    private String address;

    @Schema(description = "分包商网址")
    @Column
    private String website;

    @Schema(description = "分包商资质")
    @Column
    private String intelligence;

    @Schema(description = "分包商图标 文件 ID")
    @Column
    private Long subContractorLogo;

    public Long getSubContractorLogo() {
        return subContractorLogo;
    }

    public void setSubContractorLogo(Long subContractorLogo) {
        this.subContractorLogo = subContractorLogo;
    }

    public String getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(String intelligence) {
        this.intelligence = intelligence;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
