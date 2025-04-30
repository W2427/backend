package com.ose.auth.entity;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 用户代理字符串数据实体类。
 */
@Entity
@Table(
    name = "user_agent_strings",
    indexes = {
        @Index(
            name = "ua",
            columnList = "userAgent",
            unique = true
        )
    }
)
public class UserAgent extends BaseEntity {

    private static final long serialVersionUID = 8603340310704615836L;

    @Schema(description = "最后更新的用户代理字符串 ID")
    @Column(nullable = false, length = 500)
    private String userAgent;

    public UserAgent() {
        super();
    }

    public UserAgent(String userAgent) {
        super();
        this.setUserAgent(userAgent);
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}
