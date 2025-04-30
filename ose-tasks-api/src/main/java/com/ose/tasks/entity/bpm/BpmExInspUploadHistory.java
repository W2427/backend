package com.ose.tasks.entity.bpm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "bpm_external_inspection_upload_history",
indexes = {
    @Index(columnList = "orgId,projectId,operator,createdAt")
})
public class BpmExInspUploadHistory extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    private Long orgId;

    private Long projectId;

    private Long fileId;

    private String filePath;

    private Long operator;

    private String operatorName;

    private String memo;

    private String fileName;

    @Column(nullable = false, columnDefinition = "BIT DEFAULT 1")
    private Boolean confirmed;

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

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
