package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 材料出库单。
 */
@Entity
@Table(name = "mm_issue",
    indexes = {
        @Index(columnList = "orgId,projectId,id,status")
    })
public class MmIssueEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 2013596955550643854L;

    @Schema(description = "组织ID")
    @Column
    public Long orgId;

    @Schema(description = "项目ID")
    @Column
    public Long projectId;

    @Schema(description = "项目ID")
    @Column
    public Long companyId;

    @Schema(description = "出库单编号")
    @Column
    private String no;

    @Schema(description = "出库单名称")
    @Column
    private String name;

    @Schema(description = "流水号")
    private Integer seqNumber;

    @Schema(description = "备注")
    @Column
    private String remarks;

    @Schema(description = "出库类型")
    @Column
    private String issueType;

    @Schema(description = "出库状态")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private EntityStatus runningStatus;

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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public EntityStatus getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(EntityStatus runningStatus) {
        this.runningStatus = runningStatus;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }
}
