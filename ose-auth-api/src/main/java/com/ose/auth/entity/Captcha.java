package com.ose.auth.entity;

import com.ose.entity.CaptchaBase;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 图形验证码实体类。
 */
@Entity
@Table(name = "captcha")
public class Captcha extends CaptchaBase {

    private static final long serialVersionUID = 6736294986218993134L;

    @Schema(description = "图形验证码是否已被用户校验")
    @Column(nullable = false)
    private boolean verified = false;

    /**
     * 构造方法。
     */
    public Captcha() {
        super();
    }

    /**
     * 构造方法。
     *
     * @param id ID
     */
    public Captcha(Long id) {
        super(id);
    }

    /**
     * 取得图形验证码是否已被用户校验。
     *
     * @return 图形验证码是否已被用户校验
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * 设置图形验证码是否已被用户校验。
     *
     * @param verified 图形验证码是否已被用户校验
     */
    public void setVerified(boolean verified) {
        this.verified = verified;
    }

}
