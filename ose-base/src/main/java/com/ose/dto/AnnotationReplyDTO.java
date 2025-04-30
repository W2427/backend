package com.ose.dto;

public class AnnotationReplyDTO  extends BaseDTO {

    private static final long serialVersionUID = 1961051474645116759L;

    private String annotationUuid;

    private String reply;

    private String replyUuid;

    private Long docId;

    private boolean deleted = false;

    private Long userId;

    private String userName;

    private Integer seq;

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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
}
