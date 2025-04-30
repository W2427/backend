package com.ose.issues.dto;

import com.ose.dto.BaseDTO;
import com.ose.issues.entity.Experience;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;

import java.util.Date;

public class ExperienceMailListDTO extends BaseDTO {

    private static final long serialVersionUID = -8054878503362962987L;

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

    @Schema(description = "经验教训")
    private Experience experience;

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

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
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

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }
}
