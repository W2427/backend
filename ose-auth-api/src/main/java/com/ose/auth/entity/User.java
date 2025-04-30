package com.ose.auth.entity;

import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户数据实体类。
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "users",
    indexes = {
        @Index(columnList = "username,deleted"),
        @Index(columnList = "email,deleted"),
        @Index(columnList = "mobile,deleted")
    }
)
@Data
public class User extends UserBase {

    private static final long serialVersionUID = 114594213369073085L;

    @Schema(description = "登录密码")
    @Column(nullable = false, length = 60)
    private String password;

    @Schema(description = "用户登录账户是否已被停用")
    @Column(nullable = false)
    private boolean disabled = false;

    /**
     * 默认构造方法。
     */
    public User() {
        this(null);
    }

    /**
     * 构造方法。
     *
     * @param id 用户 ID
     */
    public User(Long id) {
        super(id);
    }

    /**
     * 取得是否已被停用的标记。
     *
     * @return 是否已被停用
     */
    public boolean getDisabled() {
        return disabled;
    }

    /**
     * 设置是否已被停用的标记。
     *
     * @param disabled 是否已被停用
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * 取得登录密码。
     *
     * @return 登录密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置登录密码。
     *
     * @param password 登录密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 校验用户数据。
     */
    public void validate() {

        if (StringUtils.isEmpty(this.getUsername(), true)
            && StringUtils.isEmpty(this.getMobile(), true)) {
            throw new Error("username is required"); // TODO
        }

    }

}
