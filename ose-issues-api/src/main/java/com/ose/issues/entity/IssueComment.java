package com.ose.issues.entity;

import com.ose.entity.BaseVersionedBizEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "issue_comments")
public class IssueComment extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -4160567141956942952L;

    @Column
    private Long issueId;

    @Column(length = 5000)
    private String comment;

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
