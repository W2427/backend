package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * 领料单实体类。
 */
@Entity
@Table(name = "mat_f_material_requisition")
public class FMaterialRequisitionEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "SPM 合同ID")
    private String spmFahId;

    @Schema(description = "SPM 领料单号")
    private String spmFahCode;

    @Schema(description = "编制人")
    private String spmUserId;

    @Schema(description = "")
    private String spmRunNumber;

    @Schema(description = "")
    private String dpId;

    @Schema(description = "")
    private String lstId;

    @Schema(description = "")
    private String fahType;

    @Schema(description = "")
    private String jobStatus;

    @Schema(description = "流程id,123456")
    private String actInstanceId;

    @Schema(description = "结构套料方案id")
    private Long fMaterialStructureNestId;

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

    public String getSpmFahId() {
        return spmFahId;
    }

    public void setSpmFahId(String spmFahId) {
        this.spmFahId = spmFahId;
    }

    public String getSpmFahCode() {
        return spmFahCode;
    }

    public void setSpmFahCode(String spmFahCode) {
        this.spmFahCode = spmFahCode;
    }

    public String getSpmUserId() {
        return spmUserId;
    }

    public void setSpmUserId(String spmUserId) {
        this.spmUserId = spmUserId;
    }

    public String getSpmRunNumber() {
        return spmRunNumber;
    }

    public void setSpmRunNumber(String spmRunNumber) {
        this.spmRunNumber = spmRunNumber;
    }

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public String getLstId() {
        return lstId;
    }

    public void setLstId(String lstId) {
        this.lstId = lstId;
    }

    public String getFahType() {
        return fahType;
    }

    public void setFahType(String fahType) {
        this.fahType = fahType;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
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

    public String getActInstanceId() {
        return actInstanceId;
    }

    public void setActInstanceId(String actInstanceId) {
        this.actInstanceId = actInstanceId;
    }

    public Long getfMaterialStructureNestId() {
        return fMaterialStructureNestId;
    }

    public void setfMaterialStructureNestId(Long fMaterialStructureNestId) {
        this.fMaterialStructureNestId = fMaterialStructureNestId;
    }
}
