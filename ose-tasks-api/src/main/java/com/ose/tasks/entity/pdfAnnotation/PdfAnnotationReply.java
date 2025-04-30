package com.ose.tasks.entity.pdfAnnotation;

import com.ose.entity.BaseBizEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(
    name = "pdf_annotation_reply",
    indexes = {
        @Index(columnList = "project_id, reply_uuid")
    }
)
public class PdfAnnotationReply  extends BaseBizEntity {
    private static final long serialVersionUID = 9064895764908103174L;

    // 项目 ID
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    // 组织 ID
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    @Column
    private Long docId;

    @Column(name="page_no")
    private Integer pageNo;

    @Column
    private Boolean deleted=false;

    @Column
    private Integer seq;

    @Column(name = "annotation_id", nullable = false)
    private Long annotationId;

    @Column(name = "reply", nullable = false, length = 500)
    private String reply;

    @Column
    private String annotationUuid;

    @Column
    private Long userId;

    @Column(name="reply_uuid")
    private String replyUuid;

    @Column
    private String userName;

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

    public Long getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(Long annotationId) {
        this.annotationId = annotationId;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
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

    public String getAnnotationUuid() {
        return annotationUuid;
    }

    public void setAnnotationUuid(String annotationUuid) {
        this.annotationUuid = annotationUuid;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getReplyUuid() {
        return replyUuid;
    }

    public void setReplyUuid(String replyUuid) {
        this.replyUuid = replyUuid;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
