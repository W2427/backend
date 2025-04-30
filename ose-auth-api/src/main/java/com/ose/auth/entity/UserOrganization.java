package com.ose.auth.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.auth.vo.OrgType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity
@Table(name = "user_organization_relations")
public class UserOrganization extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -6284288380776860547L;

    @Column
    private Long userId;

    @Column
    private Long organizationId;

    @Column(length = 1020)
    private String orgPath;

    @Schema(description = "是否为默认")
    @Column
    private boolean isDefault;

    @Schema(description = "组织类型")
    @Column
    @Enumerated(EnumType.STRING)
    private OrgType organizationType;

    // TODO: JsonIgnore
    @Transient
    private List<Long> roleIds;

    @Column
    private Boolean applyRole = false;

    @Column
    private Boolean idc;

    public UserOrganization() {
        this(null);
    }

    public UserOrganization(Long id) {
        super(id);
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    // TODO: roleIds -> roles
    // TODO: 方法名修正
    @Schema(description = "角色列表")
    @JsonProperty(value = "roleIds", access = READ_ONLY)
    public List<ReferenceData> getRoleReferences() {

        if (this.roleIds == null || this.roleIds.size() == 0) {
            return new ArrayList<>();
        }

        List<ReferenceData> references = new ArrayList<>();

        for (Long roleId : this.roleIds) {
            references.add(new ReferenceData(roleId));
        }
        return references;
    }

    // TODO: userId -> user
    // TODO: 方法名修正
    @Schema(description = "用户")
    @JsonProperty(value = "userId", access = READ_ONLY)
    public ReferenceData getUserReference() {

        if (this.userId == null) {
            return null;
        }

        return new ReferenceData(this.userId);
    }

    // TODO: organizationId -> organization
    @Schema(description = "所属组织")
    @JsonProperty(value = "organizationId", access = READ_ONLY)
    public ReferenceData getOrgReference() {

        if (this.organizationId == null) {
            return null;
        }

        return new ReferenceData(this.organizationId);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrgPath() {
        return orgPath;
    }

    public void setOrgPath(String orgPath) {
        this.orgPath = orgPath;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public OrgType getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(OrgType organizationType) {
        this.organizationType = organizationType;
    }

    public Boolean getApplyRole() {
        return applyRole;
    }

    public void setApplyRole(Boolean applyRole) {
        this.applyRole = applyRole;
    }

    public Boolean getIdc() {
        return idc;
    }

    public void setIdc(Boolean idc) {
        this.idc = idc;
    }
}
