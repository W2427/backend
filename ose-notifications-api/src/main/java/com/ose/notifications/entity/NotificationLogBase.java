package com.ose.notifications.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.constant.JsonFormatPattern;
import com.ose.entity.BaseEntity;
import com.ose.notifications.vo.NotificationSendStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import java.util.Date;

import static com.ose.notifications.vo.NotificationSendStatus.PENDING;
import static com.ose.notifications.vo.NotificationSendStatus.SENDING;

/**
 * 通知记录数据实体。
 */
@MappedSuperclass
public class NotificationLogBase extends BaseEntity {

    private static final long serialVersionUID = -4522290928337320130L;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "批次 ID")
    @Column(nullable = false)
    private Long batchId;

    @Schema(description = "目标用户 ID")
    @Column(nullable = false)
    private Long userId;

    @Schema(description = "目标用户姓名")
    @Column(length = 128, nullable = false)
    private String userName;

    @Schema(description = "目标用户电子邮箱地址")
    @Column(length = 128)
    private String email;

    @Schema(description = "电子邮件发送时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    @Column
    private Date emailSentAt;

    @Schema(description = "电子邮件发送状态")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private NotificationSendStatus emailSendStatus;

    @Schema(description = "目标用户手机好吗")
    @Column(length = 15)
    private String mobile;

    @Schema(description = "短信发送时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    @Column
    private Date smsSentAt;

    @Schema(description = "短信发送状态")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private NotificationSendStatus smsSendStatus;

    @Schema(description = "通知 ID")
    @Column
    private Long notificationId;

    @Schema(description = "阅读时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    @Column
    private Date readAt;

    @Schema(description = "是否已读")
    @Column(name = "read_by_user")
    private Boolean read = false;

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

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getEmailSentAt() {
        return emailSentAt;
    }

    public void setEmailSentAt(Date emailSentAt) {
        this.emailSentAt = emailSentAt;
    }

    public NotificationSendStatus getEmailSendStatus() {
        return emailSendStatus;
    }

    public void setEmailSendStatus(NotificationSendStatus emailSendStatus) {
        this.emailSendStatus = emailSendStatus;
    }

    /**
     * 将电子邮件发送状态更新为发送中。
     *
     * @return 是否更新成功
     */
    @JsonIgnore
    public boolean setEmailSendStatusAsSending() {

        if (emailSendStatus == PENDING) {
            emailSendStatus = SENDING;
            return true;
        }

        return false;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getSmsSentAt() {
        return smsSentAt;
    }

    public void setSmsSentAt(Date smsSentAt) {
        this.smsSentAt = smsSentAt;
    }

    public NotificationSendStatus getSmsSendStatus() {
        return smsSendStatus;
    }

    public void setSmsSendStatus(NotificationSendStatus smsSendStatus) {
        this.smsSendStatus = smsSendStatus;
    }

    /**
     * 将短信发送状态更新为发送中。
     *
     * @return 是否更新成功
     */
    @JsonIgnore
    public boolean setSmsSendStatusAsSending() {

        if (smsSendStatus == PENDING) {
            smsSendStatus = SENDING;
            return true;
        }

        return false;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Date getReadAt() {
        return readAt;
    }

    public void setReadAt(Date readAt) {
        this.readAt = readAt;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

}
