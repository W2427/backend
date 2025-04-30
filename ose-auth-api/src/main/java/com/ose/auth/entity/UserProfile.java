package com.ose.auth.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户资料数据实体类。
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
public class UserProfile extends UserBase {

    private static final long serialVersionUID = 4010872015667754671L;

    @Transient
    private List<Long> roleIds;

    @Transient
    private List<Integer> positionIds;

    @Transient
    private Boolean idc;

    /**
     * 构造方法。
     */
    public UserProfile() {
        this(null);
    }

    /**
     * 构造方法。
     *
     * @param id 用户 ID
     */
    public UserProfile(Long id) {
        super(id);
    }

    /**
     * 获取角色ID的索引形式。
     */
    @JsonProperty(value = "roleIds", access = JsonProperty.Access.READ_ONLY)
    public List<ReferenceData> getRoleReferences() {

        if (this.roleIds == null || this.roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<ReferenceData> roles = new ArrayList<>();
        for (Long roleId : this.roleIds) {
            roles.add(new ReferenceData(roleId));
        }

        return roles;
    }

    @JsonProperty(value = "positionIds", access = JsonProperty.Access.READ_ONLY)
    public List<ReferenceData> getPositionReferences() {

        if (this.positionIds == null || this.positionIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<ReferenceData> positions = new ArrayList<>();
        for (Integer positionId : this.positionIds) {
            positions.add(new ReferenceData(Long.valueOf(positionId)));
        }

        return positions;
    }
}
