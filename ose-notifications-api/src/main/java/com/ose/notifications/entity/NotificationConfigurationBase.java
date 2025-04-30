package com.ose.notifications.entity;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.notifications.vo.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 通知配置数据实体。
 */
@MappedSuperclass
public class NotificationConfigurationBase extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -1113308435665249485L;

    @Schema(description = "组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "通知类型")
    @Column(nullable = false, length = 45)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Schema(description = "消息模板 ID")
    @Column(nullable = false)
    private Long templateId;

    @Schema(description = "是否为公告")
    @Column(nullable = false)
    private Boolean announcement = true;

    @Schema(description = "向工作组发送通知时规定的成员的权限集合，成员需至少拥有一项指定的权限")
    @Column(length = 1020)
    private String memberPrivileges;

    @Schema(description = "是否发送系统内部消息")
    @Column(nullable = false)
    private Boolean sendMessage;

    @Schema(description = "是否发送电子邮件")
    @Column(nullable = false)
    private Boolean sendEmail;

    @Schema(description = "是否发送手机短信")
    @Column(name = "send_sms", nullable = false)
    private Boolean sendSMS;

    @Schema(description = "备注")
    @Lob
    @Column(length = 1020)
    private String remarks;

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

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Boolean getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(Boolean announcement) {
        this.announcement = announcement;
    }

    public String getMemberPrivileges() {
        return memberPrivileges;
    }

    public Set<String> getMemberPrivilegeSet() {

        Set<String> privileges = null;

        if (memberPrivileges != null) {
            privileges = new HashSet<>(Arrays.asList(memberPrivileges.split(",")));
            privileges.remove("");
        }

        return privileges;
    }

    @JsonSetter
    public void setMemberPrivileges(String memberPrivileges) {
        this.memberPrivileges = memberPrivileges;
    }

    public void setMemberPrivileges(Set<String> memberPrivileges) {
        if (memberPrivileges == null || memberPrivileges.size() == 0) {
            this.memberPrivileges = null;
        } else {
            this.memberPrivileges = String.join(",", memberPrivileges);
        }
    }

    public Boolean getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(Boolean sendMessage) {
        this.sendMessage = sendMessage;
    }

    public Boolean getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Boolean getSendSMS() {
        return sendSMS;
    }

    public void setSendSMS(Boolean sendSMS) {
        this.sendSMS = sendSMS;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}

