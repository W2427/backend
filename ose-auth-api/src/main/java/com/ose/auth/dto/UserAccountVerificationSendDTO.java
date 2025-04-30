package com.ose.auth.dto;

import com.ose.auth.vo.VerificationPurpose;
import com.ose.dto.BaseDTO;
import com.ose.util.CaptchaUtils;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 验证码获取请求表单。
 */
public class UserAccountVerificationSendDTO extends BaseDTO {

    private static final long serialVersionUID = 1824081266909177022L;

    @Schema(description = "验证码使用目的")
    private VerificationPurpose purpose;

    @Schema(description = "图形验证码")
    private CaptchaUtils.CaptchaDTO captcha;

    public VerificationPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(VerificationPurpose purpose) {
        this.purpose = purpose;
    }

    public CaptchaUtils.CaptchaDTO getCaptcha() {
        return captcha;
    }

    public void setCaptcha(CaptchaUtils.CaptchaDTO captcha) {
        this.captcha = captcha;
    }

}
