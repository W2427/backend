package com.ose.notifications.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.notifications.dto.receiver.UserReceiverDTO;
import com.ose.notifications.vo.NotificationContentType;
import com.ose.notifications.vo.NotificationSendStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 通知记录数据实体。
 */
@Entity
@Table(name = "logs")
public class NotificationLog extends NotificationLogBase {

    private static final long serialVersionUID = -4957423703354893579L;

    @OneToOne
    @JoinColumns({
        @JoinColumn(updatable = false, insertable = false, name = "orgId", referencedColumnName = "orgId"),
        @JoinColumn(updatable = false, insertable = false, name = "projectId", referencedColumnName = "projectId"),
        @JoinColumn(updatable = false, insertable = false, name = "batchId", referencedColumnName = "id")
    })
    private NotificationBatch batch;

    @OneToOne
    @JoinColumns({
        @JoinColumn(updatable = false, insertable = false, name = "orgId", referencedColumnName = "orgId"),
        @JoinColumn(updatable = false, insertable = false, name = "projectId", referencedColumnName = "projectId"),
        @JoinColumn(updatable = false, insertable = false, name = "notificationId", referencedColumnName = "id")
    })
    private Notification notification;

    public NotificationLog() {
        super();
    }

    public NotificationLog(NotificationBatch batch, UserReceiverDTO user, Long notificationId) {
        this();
        setOrgId(batch.getOrgId());
        setProjectId(batch.getProjectId());
        setBatchId(batch.getId());
        setUserId(user.getUserId());
        setUserName(user.getUser().getName());
        setEmail(user.getEmail());
        setEmailSendStatus(getEmail() == null ? null : NotificationSendStatus.PENDING);
        setMobile(user.getMobile());
        setSmsSendStatus(getMobile() == null ? null : NotificationSendStatus.PENDING);
        setNotificationId(notificationId);
    }

    public NotificationBatch getBatch() {
        return batch;
    }

    public void setBatch(NotificationBatch batch) {
        this.batch = batch;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    /**
     * 取得模版信息。
     *
     * @return 模版信息
     */
    @JsonIgnore
    public NotificationTemplate getTemplate() {
        return batch == null ? null : batch.getTemplate();
    }

    @Schema(description = "通知内容类型")
    @JsonProperty("contentType")
    public NotificationContentType getContentType() {
        return getTemplate() == null ? null : getTemplate().getContentType();
    }

    @Schema(description = "通知标题")
    @JsonProperty("title")
    public String getTitle() {
        return getNotification() == null ? "" : getNotification().getTitle();
    }

    @Schema(description = "通知内容")
    @JsonProperty("content")
    public String getContent() {
        return getNotification() == null ? "" : getNotification().getContent();
    }

//    @Schema(description = "通知短信内容")
//    @JsonProperty("text")
//    public String getText() {
//        return getNotification() == null ? "" : getNotification().getText();
//    }

}
