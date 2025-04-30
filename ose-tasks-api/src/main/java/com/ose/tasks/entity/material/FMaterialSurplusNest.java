package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "mat_f_material_surplus_nest")
public class FMaterialSurplusNest extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 8122581908339463794L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "流水号")
    private Integer seqNumber;

    @Schema(description = "余料套料清单编号")
    private String no;

    @Schema(description = "余料领料单编号")
    private String fMaterialSurplusReceiptsNo;

    @Schema(description = "余料领料单id")
    private Long fMaterialSurplusReceiptsId;

    @Schema(description = "备注")
    private String memo;

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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getfMaterialSurplusReceiptsNo() {
        return fMaterialSurplusReceiptsNo;
    }

    public void setfMaterialSurplusReceiptsNo(String fMaterialSurplusReceiptsNo) {
        this.fMaterialSurplusReceiptsNo = fMaterialSurplusReceiptsNo;
    }

    public Long getfMaterialSurplusReceiptsId() {
        return fMaterialSurplusReceiptsId;
    }

    public void setfMaterialSurplusReceiptsId(Long fMaterialSurplusReceiptsId) {
        this.fMaterialSurplusReceiptsId = fMaterialSurplusReceiptsId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Schema(description = "材料准备信息")
    @JsonProperty(value = "fMaterialSurplusReceiptsId")
    public ReferenceData getFMaterialSurplusReceiptsIdRef() {
        return this.fMaterialSurplusReceiptsId == null
            ? null
            : new ReferenceData(this.fMaterialSurplusReceiptsId);
    }
}
