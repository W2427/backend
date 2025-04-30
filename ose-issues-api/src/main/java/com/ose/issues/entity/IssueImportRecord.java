package com.ose.issues.entity;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "issue_import_records")
public class IssueImportRecord extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5162667719382007709L;

    @Schema(description = "组织ID")
    @Column(length = 16)
    private Long orgId;

    @Schema(description = "项目ID")
    @Column(length = 16)
    private Long projectId;

    @Schema(description = "批次编号")
    @Column
    private String batchNo;

    @Schema(description = "文件ID")
    @Column(length = 16)
    private Long fileId;

    @Schema(description = "总条数")
    @Column
    private int totalCount;

    @Schema(description = "SKIP条数")
    @Column
    private int skipCnt;

    @Schema(description = "处理的条数")
    @Column
    private int processedCount;

    @Schema(description = "错误条数")
    @Column
    private int errorCount;

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

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getProcessedCount() {
        return processedCount;
    }

    public void setProcessedCount(int processedCount) {
        this.processedCount = processedCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public int getSkipCnt() {
        return skipCnt;
    }

    public void setSkipCnt(int skipCnt) {
        this.skipCnt = skipCnt;
    }
}
