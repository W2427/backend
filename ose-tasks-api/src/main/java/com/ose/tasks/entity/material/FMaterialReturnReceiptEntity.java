package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.Date;

/**
 * 退库清单表
 */
@Entity
@Table(name = "mat_f_material_return_receipt")
public class FMaterialReturnReceiptEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "流水号")
    private Integer seqNumber;

    @Schema(description = "退库清单号")
    private String rtiCode;

    @Schema(description = "退库清单提交时间")
    private Date rtiPostDate;

    @Schema(description = "SPM是否已经退库")
    private Boolean spmSavedFlg;

    @Schema(description = "分包商")
    private String companyCode;

    @Schema(description = "分包商ID")
    private String companyId;

    @Schema(description = "默认值：N")
    private String esiStatus;

    @Schema(description = "版本")
    private String revisionId;

    @Schema(description = "默认值：N")
    private String poplIshByProc;

    @Schema(description = "短描述")
    private String shortDesc;

    @Schema(description = "长描述")
    private String description;

    private Long processId;

    private Long actInstId;

    private boolean isFinished;

    @Transient
    private String taskDefKey;

    @Transient
    private String taskId;

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

    public String getRtiCode() {
        return rtiCode;
    }

    public void setRtiCode(String rtiCode) {
        this.rtiCode = rtiCode;
    }

    public Boolean getSpmSavedFlg() {
        return spmSavedFlg;
    }

    public void setSpmSavedFlg(Boolean spmSavedFlg) {
        this.spmSavedFlg = spmSavedFlg;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Date getRtiPostDate() {
        return rtiPostDate;
    }

    public void setRtiPostDate(Date rtiPostDate) {
        this.rtiPostDate = rtiPostDate;
    }

    public String getEsiStatus() {
        return esiStatus;
    }

    public void setEsiStatus(String esiStatus) {
        this.esiStatus = esiStatus;
    }

    public String getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
    }

    public String getPoplIshByProc() {
        return poplIshByProc;
    }

    public void setPoplIshByProc(String poplIshByProc) {
        this.poplIshByProc = poplIshByProc;
    }
}
