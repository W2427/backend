package com.ose.tasks.entity.pdfAnnotation;

import com.ose.entity.BaseBizEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(
    name = "pdf_annotation",
    indexes = {
        @Index(columnList = "project_id, annotation_uuid")
    }
)
public class PdfAnnotation extends BaseBizEntity {


    private static final long serialVersionUID = -8115495735567894155L;

    // 项目 ID
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    // 组织 ID
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    // 组织 ID
    @Column(name = "docId", nullable = false)
    private Long docId;

    @Column
    private String docNo;

    // 注释的文字内容，对应 text
    @Column(name = "comment", nullable = false, length = 500)
    private String comment;

    @Column
    private Boolean closed = false;

    @Column
    private Long userId;

    @Column
    private String userName;

    @Column
    private Boolean deleted;

    @Column(name = "annotation_uuid")
    private String annotationUuid;

    @Column
    private Integer pageNo;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public String getAnnotationUuid() {
        return annotationUuid;
    }

    public void setAnnotationUuid(String annotationUuid) {
        this.annotationUuid = annotationUuid;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }
}
