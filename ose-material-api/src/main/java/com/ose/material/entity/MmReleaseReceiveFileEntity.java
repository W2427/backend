package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 入库单文件
 */
@Entity
@Table(name = "mm_release_receive_file")
public class MmReleaseReceiveFileEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 8623824507193557044L;

    @Schema(description = "组织ID")
    @Column
    public Long orgId;

    @Schema(description = "项目ID")
    @Column
    public Long projectId;

    @Schema(description = "公司ID")
    @Column
    public Long companyId;

    @Schema(description = "入库单ID")
    @Column
    private Long releaseReceiveId;

    @Schema(description = "文件类型")
    @Column
    private String fileType;

    @Schema(description = "文件ID")
    @Column
    private Long fileId;

    @Schema(description = "文件名")
    @Column
    private String fileName;

    @Schema(description = "文件path")
    @Column
    private String filePath;

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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getReleaseReceiveId() {
        return releaseReceiveId;
    }

    public void setReleaseReceiveId(Long releaseReceiveId) {
        this.releaseReceiveId = releaseReceiveId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
