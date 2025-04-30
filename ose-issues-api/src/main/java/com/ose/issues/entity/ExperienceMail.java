package com.ose.issues.entity;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 邮件mail实体
 */
@Entity
@Table(name = "experience_mails")

public class ExperienceMail extends BaseVersionedBizEntity {


    private static final long serialVersionUID = 5339983022196574018L;

    @Schema(description = "创建时间")
    @Column
    private Date createdAt;

    @Schema(description = "创建人")
    @Column
    private Long createdBy;

    @Schema(description = "修改时间")
    @Column
    private Date lastModifiedAt;

    @Schema(description = "修改人")
    @Column
    private Long lastModifiedBy;

    @Schema(description = "组织id")
    @Column
    private Long orgId;

    @Schema(description = "项目id")
    @Column
    private Long projectId;

    @Schema(description = "经验教训id")
    @Column
    private Long experienceId;

    @Schema(description = "收件人地址")
    @Column
    private String toAddress;
    @Schema(description = "抄送人地址")
    @Column
    private String ccAddress;
    @Schema(description = "发件人地址")
    @Column
    private String fromAddress;
    @Schema(description = "主题")
    @Column
    private String mailSubject;
    @Schema(description = "备注说明")
    @Column
    private String comments;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

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

    public Long getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(Long experienceId) {
        this.experienceId = experienceId;
    }

    @Override
    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    @Override
    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }
}
