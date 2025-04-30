package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.vo.qc.ReportSubType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 放行单 实体类。
 */
@Entity
@Table(name = "mat_release_note")
public class ReleaseNoteEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "放行单号")
    private String relnCode;

    private Integer seqNumber;

    @Schema(description = "材料批次号")
    private String materialBatchNumber;

    @Schema(description = "计划放行单ID, BT323ERT")
    private Long spmPlanRelnId;

    @Schema(description = "SPM 放行单ID")
    private int spmRelnId;

    @Schema(description = "放行单编号")
    private String spmRelnNumber;

    @Schema(description = "SPM 合同ID")
    private int spmPohId;

    @Schema(description = "SPM 合同编号")
    private String spmPoNumber;

    @Schema(description = "SPM 合同序号")
    private int spmPoSupp;

    @Schema(description = "报告子类型")
    @Enumerated(EnumType.STRING)
    private ReportSubType reportSubType;

    private Long processId;

    private Long actInstId;

    private boolean isFinished;

    @Transient
    private String taskId;

    @Transient
    private FMaterialStocktakeEntity fMaterialStocktakeEntity;

    @Transient
    private FMaterialOpenboxEntity fMaterialOpenboxEntity;

    public int getSpmRelnId() {
        return spmRelnId;
    }

    public void setSpmRelnId(int spmRelnId) {
        this.spmRelnId = spmRelnId;
    }

    public String getSpmRelnNumber() {
        return spmRelnNumber;
    }

    public void setSpmRelnNumber(String spmRelnNumber) {
        this.spmRelnNumber = spmRelnNumber;
    }

    public int getSpmPohId() {
        return spmPohId;
    }

    public void setSpmPohId(int spmPohId) {
        this.spmPohId = spmPohId;
    }

    public String getSpmPoNumber() {
        return spmPoNumber;
    }

    public void setSpmPoNumber(String spmPoNumber) {
        this.spmPoNumber = spmPoNumber;
    }

    public int getSpmPoSupp() {
        return spmPoSupp;
    }

    public void setSpmPoSupp(int spmPoSupp) {
        this.spmPoSupp = spmPoSupp;
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

    public Long getProcessId() {
        return processId;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getRelnCode() {
        return relnCode;
    }

    public void setRelnCode(String relnCode) {
        this.relnCode = relnCode;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public Long getSpmPlanRelnId() {
        return spmPlanRelnId;
    }

    public void setSpmPlanRelnId(Long spmPlanRelnId) {
        this.spmPlanRelnId = spmPlanRelnId;
    }

    public FMaterialStocktakeEntity getfMaterialStocktakeEntity() {
        return fMaterialStocktakeEntity;
    }

    public void setfMaterialStocktakeEntity(FMaterialStocktakeEntity fMaterialStocktakeEntity) {
        this.fMaterialStocktakeEntity = fMaterialStocktakeEntity;
    }

    public FMaterialOpenboxEntity getfMaterialOpenboxEntity() {
        return fMaterialOpenboxEntity;
    }

    public void setfMaterialOpenboxEntity(FMaterialOpenboxEntity fMaterialOpenboxEntity) {
        this.fMaterialOpenboxEntity = fMaterialOpenboxEntity;
    }

    public String getMaterialBatchNumber() {
        return materialBatchNumber;
    }

    public void setMaterialBatchNumber(String materialBatchNumber) {
        this.materialBatchNumber = materialBatchNumber;
    }

    public ReportSubType getReportSubType() {
        return reportSubType;
    }

    public void setReportSubType(ReportSubType reportSubType) {
        this.reportSubType = reportSubType;
    }
}
