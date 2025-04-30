package com.ose.tasks.entity;

import com.ose.entity.BaseVersionedBizEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "suggestion")
public class Suggestion extends BaseVersionedBizEntity {
    private static final long serialVersionUID = -182059554604029395L;
    private Long fileId;

    private String filePath;

    private String fileName;

    private Long proposer;

    private String proposerName;

    private String summary;

    private String priority;
    @Column(length = 1020)
    private String description;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getProposer() {
        return proposer;
    }

    public void setProposer(Long proposer) {
        this.proposer = proposer;
    }

    public String getProposerName() {
        return proposerName;
    }

    public void setProposerName(String proposerName) {
        this.proposerName = proposerName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
