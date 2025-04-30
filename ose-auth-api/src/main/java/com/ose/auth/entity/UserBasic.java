package com.ose.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.entity.BaseEntity;
import com.ose.util.RegExpUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

/**
 * 用户基本信息数据传输对象类。
 */
@Entity
@Table(name = "users")
public class UserBasic extends BaseEntity {

    private static final long serialVersionUID = -4016111211186752039L;

    @Schema(description = "用户类型（system；super；administrator；user）")
    private String type;

    @Schema(description = "头像地址")
    private String logo;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号码")
    @Column(length = 16)
    @Pattern(
        regexp = RegExpUtils.MOBILE,
        message = "mobile number is invalid"
    )
    private String mobile;

    @Schema(description = "电子邮箱地址")
    @Column(length = 64)
    @Pattern(
        regexp = RegExpUtils.EMAIL,
        message = "Email address is invalid"
    )
    private String email;

    @JsonIgnore
    private boolean deleted;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
