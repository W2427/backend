package com.ose.notifications.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * 通知配置创建数据传输对象。
 */
public class NotificationConfigurationPostDTO extends BaseDTO {

    private static final long serialVersionUID = -2213259596316260737L;

    @Schema(description = "是否发送系统内部消息")
    @NotNull
    private Boolean sendMessage;

    @Schema(description = "是否发送电子邮件")
    @NotNull
    private Boolean sendEmail;

    @Schema(description = "是否发送手机短信")
    @NotNull
    private Boolean sendSMS;

    @Schema(description = "消息模板 ID")
    @NotEmpty
    private Long templateId;

    @Schema(description = "是否为公告，若为公告将无法在模版中通过 <code>RECEIVER</code> 参数引用发送目标用户的信息")
    @NotNull
    private Boolean announcement;

    @Schema(description = "向工作组发送通知时规定的成员的权限集合，成员需至少拥有一项指定的权限")
    private Set<String> memberPrivileges;

    @Schema(description = "备注")
    private String remarks;

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

    public Set<String> getMemberPrivileges() {
        return memberPrivileges;
    }

    public void setMemberPrivileges(Set<String> memberPrivileges) {
        this.memberPrivileges = memberPrivileges;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
