package com.ose.tasks.entity;

import com.ose.entity.BaseVersionedBizEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_signature")
public class UserSignature extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 8455722264222411792L;

    @Column
    private Long userId;

    @Column
    private Long fileId;

    @Column
    private String filePath;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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
}
