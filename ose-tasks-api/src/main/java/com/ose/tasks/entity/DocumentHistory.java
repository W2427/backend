package com.ose.tasks.entity;

import com.ose.entity.BaseVersionedBizEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "document_history")
public class DocumentHistory extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -6025467877152561278L;

    private Long fileId;

    private String filePath;

    private String fileName;

    private Long operator;

    private String operatorName;

    private String memo;

    private String label;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
