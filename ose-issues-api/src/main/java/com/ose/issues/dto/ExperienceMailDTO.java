package com.ose.issues.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ExperienceMailDTO extends BaseDTO {

    private static final long serialVersionUID = -4134297713024494168L;

    @Schema(name = "收件人地址")
    @NotNull
    @NotEmpty
    private String toAddress;

    @Schema(name = "抄送人地址")
    @NotNull
    @NotEmpty
    private String ccAddress;

    @Schema(name = "发件人地址")
    @NotNull
    @NotEmpty
    private String fromAddress;

    @Schema(name = "主题")
    @NotNull
    @NotEmpty
    private String mailSubject;

    @Schema(name = "备注说明")
    private String comments;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }
}
