package com.ose.notifications.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ose.constant.JsonFormatPattern;
import com.ose.entity.BaseBizEntity;
import com.ose.notifications.vo.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 通知记录数据实体。
 */
@Entity
@Table(name = "user_notifications")
public class UserNotification extends BaseBizEntity {

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

    @Schema(description = "发送时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    @Column
    private Date sentAt;

    @Schema(description = "标题")
    @Column
    private String title;

    @Schema(description = "内容")
    @Column
    private String content;

    @Schema(description = "发送者 ID")
    @Column
    private Long createdBy;

    @Schema(description = "发送者姓名")
    @Column
    private String creatorName;

    @Schema(description = "接收者 ID")
    @Column
    private Long userId;

    @Schema(description = "邮件发送时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    @Column
    private Date emailSentAt;

    @Schema(description = "短信发送时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    @Column
    private Date smsSentAt;

    @Schema(description = "是否已读")
    @Column(name = "read_by_user")
    private Boolean read;

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

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getEmailSentAt() {
        return emailSentAt;
    }

    public void setEmailSentAt(Date emailSentAt) {
        this.emailSentAt = emailSentAt;
    }

    public Date getSmsSentAt() {
        return smsSentAt;
    }

    public void setSmsSentAt(Date smsSentAt) {
        this.smsSentAt = smsSentAt;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
