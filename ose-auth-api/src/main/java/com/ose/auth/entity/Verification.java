package com.ose.auth.entity;

import com.ose.auth.vo.AccountType;
import com.ose.auth.vo.VerificationPurpose;
import com.ose.entity.BaseEntity;
import com.ose.util.RegExpUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 电子邮件/短信等验证码数据实体类。
 */
@Entity
@Table(
    name = "verification_codes",
    indexes = {
        @Index(
            name = "account_purpose",
            columnList = "account,purpose",
            unique = true
        ),
        @Index(
            name = "account_purpose_code",
            columnList = "account,purpose,code"
        )
    }
)
public class Verification extends BaseEntity {

    private static final long serialVersionUID = 3125707939153268116L;

    @Schema(description = "目标账号类型")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Schema(description = "目标账号（电子邮箱地址、手机号码等）")
    @Column(nullable = false)
    private String account;

    @Schema(description = "验证目的")
    @Column(nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private VerificationPurpose purpose;

    @Schema(description = "验证码")
    @Column(nullable = false, length = 32)
    private String code;

    @Schema(description = "创建时间（Unix Epoch 时间，毫秒）")
    @Column(nullable = false)
    private long createdAt;

    @Schema(description = "有效截止时间（Unix Epoch 时间，毫秒）")
    @Column(nullable = false)
    private long validUntil;

    /**
     * 构造方法。
     */
    public Verification(
        String account,
        VerificationPurpose purpose,
        String code,
        long validUntil
    ) {
        this();
        setAccount(account);
        setPurpose(purpose);
        setCode(code);
        setValidUntil(validUntil);
    }

    /**
     * 默认构造方法。
     */
    public Verification() {
        super();
        createdAt = System.currentTimeMillis();
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {

        if (RegExpUtils.isEmailAddress(account)) {
            setType(AccountType.EMAIL);
        } else if (RegExpUtils.isMobileNumber(account)) {
            setType(AccountType.MOBILE);
        } else {
            setType(null);
        }

        this.account = account;
    }

    public VerificationPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(VerificationPurpose purpose) {
        this.purpose = purpose;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(long validUntil) {
        this.validUntil = validUntil;
    }

}
