package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 材料质量证明书实体类。
 */
@Entity
@Table(name = "mat_release_note_quality_certificate",
indexes = {
    @Index(columnList = "org_id, project_id, cert_code, page_number")
})
public class ReleaseNoteQualityCertificateEntity extends BaseVersionedBizEntity {
    /**
     *
     */
    private static final long serialVersionUID = 7953595488423198347L;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "cert_code")
    private String certCode;

    @Column(name = "page_number")
    private String pageNumber;

    @Column(name = "reln_id")
    private Long relnId;

    @Column(name = "cert_desc")
    private String certDesc;

    @Column(name = "cert_name")
    private String certName;

    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "file_path")
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

    public String getCertCode() {
        return certCode;
    }

    public void setCertCode(String certCode) {
        this.certCode = certCode;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Long getRelnId() {
        return relnId;
    }

    public void setRelnId(Long relnId) {
        this.relnId = relnId;
    }

    public String getCertDesc() {
        return certDesc;
    }

    public void setCertDesc(String certDesc) {
        this.certDesc = certDesc;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
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
