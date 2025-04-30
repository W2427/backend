package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 炉批号。
 */
@Entity
@Table(name = "mm_heat_batch_no",
    indexes = {
        @Index(columnList = "orgId,projectId,status"),
        @Index(columnList = "orgId,projectId,id,status"),
        @Index(columnList = "orgId,projectId,heatNoCode,batchNoCode,status")
    })
public class MmHeatBatchNoEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 6973652677328158612L;

    @Schema(description = "组织ID")
    @Column
    public Long orgId;

    @Schema(description = "项目ID")
    @Column
    public Long projectId;

    @Schema(description = "炉号")
    @Column
    private String heatNoCode;

    @Schema(description = "批号")
    @Column
    private String batchNoCode;

    @Schema(description = "炉批号说明")
    @Column
    private String descs;

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

    public String getHeatNoCode() {
        return heatNoCode;
    }

    public void setHeatNoCode(String heatNoCode) {
        this.heatNoCode = heatNoCode;
    }

    public String getBatchNoCode() {
        return batchNoCode;
    }

    public void setBatchNoCode(String batchNoCode) {
        this.batchNoCode = batchNoCode;
    }

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
    }
}
