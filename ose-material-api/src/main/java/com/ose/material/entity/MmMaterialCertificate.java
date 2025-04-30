package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 材料证书表
 */
@Entity
@Table(name = "mm_material_certificate",
    indexes = {
        @Index(columnList = "orgId,projectId,id,status"),
        @Index(columnList = "orgId,projectId,no,status")
    })
public class MmMaterialCertificate extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 2144463794959453206L;

    @Schema(description = "组织id")
    @Column
    private Long orgId;

    @Schema(description = "项目id")
    @Column
    private Long projectId;

    @Schema(description = "证书编号")
    @Column
    private String no;

    @Schema(description = "证书文件地址")
    @Column
    private String path;

    @Schema(description = "证书文件名")
    @Column
    private String fileName;

    @Schema(description = "证书文件id")
    @Column
    private Long fileId;

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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
