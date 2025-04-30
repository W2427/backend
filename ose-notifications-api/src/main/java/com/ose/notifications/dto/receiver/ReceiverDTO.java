package com.ose.notifications.dto.receiver;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 消息接收者数据传输对象。
 */
public abstract class ReceiverDTO extends BaseDTO {

    private static final long serialVersionUID = 9071365319996059759L;

    @Schema(description = "是否发送内部消息（覆盖默认配置）")
    public boolean sendMessage;

    @Schema(description = "是否发送电子邮件（覆盖默认配置）")
    public Boolean sendEmail;

    @Schema(description = "是否发送短信（覆盖默认配置）")
    public Boolean sendMobile;

    public boolean isSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(boolean sendMessage) {
        this.sendMessage = sendMessage;
    }

    public Boolean getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Boolean getSendMobile() {
        return sendMobile;
    }

    public void setSendMobile(Boolean sendMobile) {
        this.sendMobile = sendMobile;
    }

}
