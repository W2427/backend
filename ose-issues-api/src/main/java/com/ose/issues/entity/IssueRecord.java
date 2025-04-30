package com.ose.issues.entity;

import com.ose.entity.BaseVersionedBizEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "issue_records")
public class IssueRecord extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -900154118986402265L;

    @Column
    private Long issueId;

    @Column(length = 2000)
    private String content;

    public IssueRecord() {
    }

    public IssueRecord(Long issueId, String content) {
        setIssueId(issueId);
        setContent(content);
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
