package com.ose.auth.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity
@Table(
    name = "user_role_relations",
    indexes = {
        @Index(columnList = "roleId")
    }
)
public class UserRole extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 4538101898675712445L;

    @Schema(description = "用户 ID")
    @Column
    private Long userId;

    // TODO: JsonIgnore
    @Column
    private Long roleId;

    // TODO: JsonIgnore
    @Transient
    private List<Long> orgIds;

    public List<Long> getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(List<Long> orgIds) {
        this.orgIds = orgIds;
    }

    // TODO: orgIds -> org

    @Schema(description = "组织列表")
    @JsonProperty(value = "orgIds", access = READ_ONLY)
    public List<ReferenceData> getReferences() {
        if (this.orgIds == null || this.orgIds.size() == 0) {
            return new ArrayList<>();
        }

        List<ReferenceData> references = new ArrayList<>();

        for (Long orgId : this.orgIds) {
            references.add(new ReferenceData(orgId));
        }
        return references;
    }

    // TODO: roleId -> role
    @Schema(description = "角色")
    @JsonProperty(value = "roleId", access = READ_ONLY)
    public ReferenceData getReference() {

        if (this.roleId == null) {
            return null;
        }

        return new ReferenceData(this.roleId);
    }

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
}
