package com.ose.notifications.entity;

import com.ose.entity.BaseBizEntity;
import com.ose.notifications.vo.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 通知记录数据实体。
 */
@Entity
@Table(name = "notifications")
public class Notification extends BaseBizEntity {

    private static final long serialVersionUID = 873748638869086966L;
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

    @Schema(description = "标题")
    @Column
    private String title;

    @Schema(description = "内容")
    @Column
    private String content;

    @Schema(description = "发送者姓名")
    @Column
    private String creatorName;

    @Schema(description = "content中的执行人")
    @Column
    private String userName;

    @Schema(description = "姓名")
    @Column
    private String name;

    @Schema(description = "消息状态")
    @Column
    private Boolean messageStatus = false;

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
