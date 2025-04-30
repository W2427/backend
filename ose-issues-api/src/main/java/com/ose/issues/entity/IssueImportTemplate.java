package com.ose.issues.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.issues.vo.IssueType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

@Entity
@Table(
    name = "issue_import_templates",
    indexes = {
        @Index(columnList = "projectId,issueType", unique = true)
    }
)
public class IssueImportTemplate extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 8518148737242430325L;

    @Schema(description = "项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "问题类型")
    @Column(length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    private IssueType issueType;

    @Schema(description = "导入文件 ID")
    @Column(nullable = false)
    private Long fileId;

    @Schema(description = "导入文件 ID")
    @Column(length = 1000)
    private String propertyNames;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getPropertyNames() {
        return propertyNames;
    }

    public void setPropertyNames(String propertyNames) {
        this.propertyNames = propertyNames;
    }
}
