package com.ose.auth.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "user_role_relations",
    indexes = {
        @Index(columnList = "deleted,userId"),
        @Index(columnList = "deleted,roleId,userId")
    }
)
public class RoleMember extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -406528319924741850L;

    @Schema(description = "用户 ID")
    @Column
    private Long userId;

    @Schema(description = "角色 ID")
    @Column
    private Long roleId;

    @Schema(description = "角色名")
    @Column
    private String name;

    @Schema(description = "用户手机号码")
    @Column
    private String mobile;

    // TODO: username（登录账号名） -> userName（用户姓名）
    @Schema(description = "用户姓名")
    @Column
    private String username;

    @Schema(description = "用户电子邮箱地址")
    @Column
    private String email;

    @Transient
    private List<Long> organizations;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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

    public List<Long> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Long> organizations) {
        this.organizations = organizations;
    }

    @Schema(description = "组织列表")
    @JsonProperty(value = "organizations", access = JsonProperty.Access.READ_ONLY)
    public List<ReferenceData> getOrgReferences() {
        if (this.getOrganizations() == null || this.getOrganizations().size() == 0) {
            return new ArrayList<>();
        }
        List<ReferenceData> references = new ArrayList<>();
        for (Long organizationId : this.organizations) {
            references.add(new ReferenceData(organizationId));
        }
        return references;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
