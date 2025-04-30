package com.ose.notifications.dto.receiver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.auth.entity.UserBasic;
import com.ose.util.RegExpUtils;
import com.ose.util.ValueUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

/**
 * 消息接收者数据传输对象。
 */
public class UserReceiverDTO extends ReceiverDTO {

    private static final long serialVersionUID = 3211493034690712069L;

    @Schema(description = "通知对象用户 ID")
    @NotEmpty
    private Long userId;

    @Schema(description = "电子邮箱地址（用于替代用户的默认电子邮箱地址）")
    @Pattern(
        regexp = RegExpUtils.EMAIL,
        message = "Email address is invalid"
    )
    private String email;

    @Schema(description = "手机号码（用于替代用户默认的手机号码）")
    @Pattern(
        regexp = RegExpUtils.MOBILE,
        message = "mobile number is invalid"
    )
    private String mobile;

    @JsonIgnore
    private UserBasic user;

    public UserReceiverDTO() {
        super();
    }

    public UserReceiverDTO(UserBasic user) {
        super();
        setUser(user);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public UserBasic getUser() {
        return user;
    }

    public UserReceiverDTO setUser(UserBasic user) {
        this.setUserId(user.getId());
        this.setEmail(ValueUtils.notNull(this.getEmail(), user.getEmail()));
        this.setMobile(ValueUtils.notNull(this.getMobile(), user.getMobile()));
        this.user = user;
        return this;
    }

}
