package com.ose.notifications.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ose.constant.JsonFormatPattern;
import com.ose.entity.BaseEntity;
import com.ose.notifications.vo.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 通知批次视图。
 */
@Entity
@Table(name = "batch_summary")
public class NotificationBatchSummary extends BaseEntity {

    private static final long serialVersionUID = 8641608439743050927L;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "通知类型")
    @Column
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Schema(description = "是否为公告")
    @Column
    private Boolean announcement = true;

    @Schema(description = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    @Column
    private Date createdAt;

    @Schema(description = "创建者 ID")
    @Column
    private Long createdBy;

    @Schema(description = "创建者姓名")
    @Column
    private String creatorName;

    @Schema(description = "发送总件数")
    @Column
    private Integer totalCount;

    @Schema(description = "待发送电子邮件数")
    @Column
    private Long emailPendingCount;

    @Schema(description = "发送中电子邮件数")
    @Column
    private Long emailSendingCount;

    @Schema(description = "已发送电子邮件数")
    @Column
    private Long emailSentCount;

    @Schema(description = "发送失败电子邮件数")
    @Column
    private Long emailFailedCount;

    @Schema(description = "待发送短信数")
    @Column
    private Long smsPendingCount;

    @Schema(description = "发送中短信数")
    @Column
    private Long smsSendingCount;

    @Schema(description = "已发送短信数")
    @Column
    private Long smsSentCount;

    @Schema(description = "发送失败短信数")
    @Column
    private Long smsFailedCount;

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

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Boolean getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(Boolean announcement) {
        this.announcement = announcement;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Long getEmailPendingCount() {
        return emailPendingCount;
    }

    public void setEmailPendingCount(Long emailPendingCount) {
        this.emailPendingCount = emailPendingCount;
    }

    public Long getEmailSendingCount() {
        return emailSendingCount;
    }

    public void setEmailSendingCount(Long emailSendingCount) {
        this.emailSendingCount = emailSendingCount;
    }

    public Long getEmailSentCount() {
        return emailSentCount;
    }

    public void setEmailSentCount(Long emailSentCount) {
        this.emailSentCount = emailSentCount;
    }

    public Long getEmailFailedCount() {
        return emailFailedCount;
    }

    public void setEmailFailedCount(Long emailFailedCount) {
        this.emailFailedCount = emailFailedCount;
    }

    public Long getSmsPendingCount() {
        return smsPendingCount;
    }

    public void setSmsPendingCount(Long smsPendingCount) {
        this.smsPendingCount = smsPendingCount;
    }

    public Long getSmsSendingCount() {
        return smsSendingCount;
    }

    public void setSmsSendingCount(Long smsSendingCount) {
        this.smsSendingCount = smsSendingCount;
    }

    public Long getSmsSentCount() {
        return smsSentCount;
    }

    public void setSmsSentCount(Long smsSentCount) {
        this.smsSentCount = smsSentCount;
    }

    public Long getSmsFailedCount() {
        return smsFailedCount;
    }

    public void setSmsFailedCount(Long smsFailedCount) {
        this.smsFailedCount = smsFailedCount;
    }

}
