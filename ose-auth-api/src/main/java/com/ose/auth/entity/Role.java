package com.ose.auth.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.auth.vo.UserPrivilege;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity
@Table(name = "roles")
public class Role extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 5045958175371536791L;

    @Schema(description = "名称")
    @Column
    private String name;

    @Schema(description = "编号")
    @Column
    private String no;

    // TODO: JsonIgnore
    @Column(length = 4096)
    private String privileges;

    @Schema(description = "排序")
    @Column
    private int sort;

    // TODO: JsonIgnore
    @Column
    private Long organizationId;

    @Column(length = 1020)
    private String orgPath;

    @Schema(description = "是否可编辑")
    @Column
    private Boolean isEditable = true;

    @Schema(description = "代码")
    @Column
    private String code;

    @Schema(description = "是否是模板")
    @Column
    private Boolean isTemplate = false;

    // TODO: organizationId -> organization
    @Schema(description = "所属组织 ID")
    @JsonProperty(value = "organizationId", access = READ_ONLY)
    public ReferenceData getOrganizationReference() {
        return this.organizationId == null ? null : new ReferenceData(this.organizationId);
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @JsonSetter
    public void setOrganizationId(ReferenceData organizationIdRef) {
        if (organizationIdRef == null) {
            return;
        }
        setOrganizationId(organizationIdRef.get$ref());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String privileges) {

        String[] privilegeArray;

        if (StringUtils.isEmpty(privileges)) {
            privilegeArray = null;
        } else {
            privilegeArray = privileges.split(",");
        }

        setPrivileges(privilegeArray);
    }

    public void setPrivileges(String[] privileges) {
        setPrivileges(
            (privileges == null || privileges.length == 0)
                ? new HashSet<>()
                : new HashSet<>(Arrays.asList(privileges))
        );
    }

    public void setPrivileges(List<UserPrivilege> privileges) {

        Set<String> privilegeSet = new HashSet<>();

        if (privileges != null) {
            for (com.ose.auth.vo.UserPrivilege privilege : privileges) {
                privilegeSet.add(privilege.toString());
            }
        }

        privilegeSet.remove(com.ose.auth.vo.UserPrivilege.NONE.toString());

        if (privilegeSet.contains(com.ose.auth.vo.UserPrivilege.ALL.toString())) {
            privilegeSet.removeAll(new HashSet<String>());
            privilegeSet.add(com.ose.auth.vo.UserPrivilege.ALL.toString());
        }

        setPrivileges(privilegeSet);
    }

    @JsonSetter
    public void setPrivileges(Set<String> privileges) {

        if (privileges == null || privileges.size() == 0) {
            this.privileges = "";
            return;
        }

        privileges.removeAll(Collections.singleton(""));

        this.privileges = "," + String.join(",", privileges) + ",";
    }

    /**
     * 获取权限列表。
     *
     * @return 拥有的权限列表
     */
    @Schema(description = "权限列表")
    @JsonProperty(value = "privileges", access = READ_ONLY)
    private Set<String> getPrivilegeSet() {

        Set<String> privilegeSet = StringUtils.isEmpty(this.privileges)
            ? new HashSet<>()
            : new HashSet<>(Arrays.asList(this.privileges.split(",")));

        privilegeSet.removeAll(Collections.singleton(""));

        return privilegeSet;
    }

    /**
     * 添加权限。
     *
     * @param privileges 添加的权限列表
     */
    public void addPrivileges(List<String> privileges) {

        if (privileges == null || privileges.size() == 0) {
            return;
        }

        Set<String> privilegeSet = getPrivilegeSet();
        privilegeSet.addAll(privileges);
        setPrivileges(privilegeSet);
    }

    /**
     * 移除权限。
     *
     * @param removePrivileges 移除的权限列表
     */
    public void removePrivileges(List<String> removePrivileges) {

        if (removePrivileges == null
            || removePrivileges.size() == 0
            || StringUtils.isEmpty(this.privileges)) {
            return;
        }

        Set<String> privilegeSet = getPrivilegeSet();
        privilegeSet.removeAll(removePrivileges);
        setPrivileges(privilegeSet);
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Boolean getEditable() {
        return isEditable;
    }

    public void setEditable(Boolean editable) {
        isEditable = editable;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getTemplate() {
        return isTemplate;
    }

    public void setTemplate(Boolean template) {
        isTemplate = template;
    }

    public String getOrgPath() {
        return orgPath;
    }

    public void setOrgPath(String orgPath) {
        this.orgPath = orgPath;
    }
}
