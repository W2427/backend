package com.ose.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * 图形验证码基类。
 */
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class CaptchaBase extends BaseEntity {

    private static final long serialVersionUID = 1958960397421754302L;

    @Schema(description = "有效截止时间（Unix Epoch 时间，毫秒）")
    @Column(nullable = false)
    private long validUntil;

    /**
     * 构造方法。
     */
    public CaptchaBase() {
        super();
    }

    /**
     * 构造方法。
     *
     * @param id ID
     */
    public CaptchaBase(Long id) {
        super(id);
    }

    /**
     * 取得有效截止时间。
     *
     * @return 有效截止时间
     */
    public long getValidUntil() {
        return validUntil;
    }

    /**
     * 设置有效截止时间。
     *
     * @param validUntil 有效截止时间
     */
    public void setValidUntil(long validUntil) {
        this.validUntil = validUntil;
    }

}
