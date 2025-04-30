package com.ose.notifications.dto;

import com.ose.dto.PageDTO;
import com.ose.notifications.vo.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class NotificationSearchDTO extends PageDTO {

    private static final long serialVersionUID = 4340772233013705629L;

    @Schema(description = "通知类型")
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Schema(description = "标题")
    private String userType;

    @Schema(description = "标题")
    private String userName;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "消息处理状态")
    private Boolean messageStatus;

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(Boolean messageStatus) {
        this.messageStatus = messageStatus;
    }
}
