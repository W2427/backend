package com.ose.notifications.entity;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.util.CryptoUtils;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

/**
 * 通知模版数据实体。
 */
@Entity
@Table(name = "templates")
public class NotificationTemplate extends NotificationTemplateBase {

    private static final long serialVersionUID = -8954820505425107055L;

    // 模版中发送者信息的参数名称
    public static final String SENDER_PARAMETER_NAME = "SENDER";

    // 模版中接收者信息的参数名称
    public static final String RECEIVER_PARAMETER_NAME = "RECEIVER";

    @Schema(description = "消息模板（富文本）")
    @Lob
    @Column(length = 102400)
    private String content;

    @Schema(description = "消息模板（纯文本）")
    @Lob
    @Column(length = 1020)
    private String text;

    @Schema(description = "备注")
    @Lob
    @Column(length = 1020)
    private String remarks;

    @Schema(description = "摘要（md5(组织 ID + 标题 + 消息模版)）")
    @Column(length = 32)
    private String hash;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getHash() {
        return hash;
    }

    @JsonSetter
    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setHash() {
        hash = CryptoUtils
            .md5(
                getOrgId()
                    + StringUtils.trim(getTitle())
                    + "\r\n\n" + StringUtils.trim(content)
                    + "\r\n\n" + StringUtils.trim(text)
            )
            .toUpperCase();
    }

}
