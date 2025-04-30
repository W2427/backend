package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "mat_f_material_nest_receipt")
public class FMaterialNestReceipt extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "流水号")
    private Integer seqNumber;

    @Schema(description = "套料清单编号")
    private String fmnrCode;

    @Schema(description = "材料准备单id")
    private Long fmpId;

    @Schema(description = "备注")
    private String fmnrDesc;

    @Schema(description = "切割损耗量")
    private Double wastage;

    @Schema(description = "管线末端放弃量")
    private Double abandonment;

    @Transient
    private String taskId;

    @Transient
    private String taskDefKey;

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

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getFmnrCode() {
        return fmnrCode;
    }

    public void setFmnrCode(String fmnrCode) {
        this.fmnrCode = fmnrCode;
    }

    public Long getFmpId() {
        return fmpId;
    }

    public void setFmpId(Long fmpId) {
        this.fmpId = fmpId;
    }

    public String getFmnrDesc() {
        return fmnrDesc;
    }

    public void setFmnrDesc(String fmnrDesc) {
        this.fmnrDesc = fmnrDesc;
    }

    public Double getWastage() {
        return wastage;
    }

    public void setWastage(Double wastage) {
        this.wastage = wastage;
    }

    public Double getAbandonment() {
        return abandonment;
    }

    public void setAbandonment(Double abandonment) {
        this.abandonment = abandonment;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    @Schema(description = "材料准备信息")
    @JsonProperty(value = "fmpId")
    public ReferenceData getFmpIdRef() {
        return this.fmpId == null
            ? null
            : new ReferenceData(this.fmpId);
    }
}
