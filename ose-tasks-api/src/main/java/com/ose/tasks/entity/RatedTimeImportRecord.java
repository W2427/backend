package com.ose.tasks.entity;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "rated_time_import_record")
public class RatedTimeImportRecord extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5162667719382007709L;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Schema(description = "组织ID")
    @Column
    private String fileName;

    @Schema(description = "组织ID")
    @Column
    private Long orgId;

    @Schema(description = "项目ID")
    @Column
    private Long projectId;

    @Schema(description = "批次编号")
    @Column
    private String batchNo;

    @Schema(description = "文件ID")
    @Column
    private Long fileId;

    @Schema(description = "成功条数")
    @Column
    private int successNum;

    @Schema(description = "失败条数")
    @Column
    private int failedNum;

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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public int getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(int successNum) {
        this.successNum = successNum;
    }

    public int getFailedNum() {
        return failedNum;
    }

    public void setFailedNum(int failedNum) {
        this.failedNum = failedNum;
    }
}
