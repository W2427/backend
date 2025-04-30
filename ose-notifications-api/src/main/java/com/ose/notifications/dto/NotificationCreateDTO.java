package com.ose.notifications.dto;

import com.ose.dto.BaseDTO;
import com.ose.notifications.vo.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class NotificationCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -7733677281850682986L;

    @Schema(description = "通知类型")
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Schema(description = "标题")
    @Column
    private String title;

    @Schema(description = "内容")
    @Column
    private String content;

    @Schema(description = "发布人")
    @Column
    private String creatorName;

    @Schema(description = "相关人")
    @Column
    private String userName;

    @Schema(description = "姓名")
    @Column
    private String name;

    @Schema(description = "消息状态")
    @Column
    private Boolean messageStatus;

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(Boolean messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
