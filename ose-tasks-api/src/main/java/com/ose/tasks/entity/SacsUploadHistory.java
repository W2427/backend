package com.ose.tasks.entity;

import com.ose.entity.BaseBizEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "sacs_upload_history",
indexes = {
    @Index(columnList = "orgId,projectId,operator,createdAt")
})
public class SacsUploadHistory extends BaseBizEntity {

    private static final long serialVersionUID = -6819965762336699772L;

    private Long orgId;

    private Long projectId;

    private Long fileId;

    private String filePath;

    private Long operator;

    private String operatorName;

    private String memo;

    private String fileName;

    private Long calculatedFileId;

    private String calculatedFilePath;

    private String calculatedFileName;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getCalculatedFileId() {
        return calculatedFileId;
    }

    public void setCalculatedFileId(Long calculatedFileId) {
        this.calculatedFileId = calculatedFileId;
    }

    public String getCalculatedFilePath() {
        return calculatedFilePath;
    }

    public void setCalculatedFilePath(String calculatedFilePath) {
        this.calculatedFilePath = calculatedFilePath;
    }

    public String getCalculatedFileName() {
        return calculatedFileName;
    }

    public void setCalculatedFileName(String calculatedFileName) {
        this.calculatedFileName = calculatedFileName;
    }
}
