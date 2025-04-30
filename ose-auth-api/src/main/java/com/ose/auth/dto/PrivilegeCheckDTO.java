package com.ose.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.auth.vo.ResourceType;
import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 权限检查数据传输对象。
 */
public class PrivilegeCheckDTO extends BaseDTO {

    private static final long serialVersionUID = 1720385481078434134L;

    @Schema(description = "是否必须已完成授权")
    private boolean required;

    @Schema(description = "授权类型，默认为【Bearer】")
    private String type;

    @Schema(description = "组织 ID")
    private Long orgId;

    @Schema(description = "资源类型")
    private ResourceType resourceType;

    @Schema(description = "资源 ID")
    private Long resourceId;

    @Schema(description = "权限列表")
    private UserPrivilege[] privileges = new UserPrivilege[]{};

    /**
     * 构造方法。
     */
    @JsonCreator
    public PrivilegeCheckDTO() {

        super();
    }

    /**
     * 构造方法。
     */
    public PrivilegeCheckDTO(
        WithPrivilege annotation,
        Long orgId,
        Long resourceId
    ) {
        this();
        setRequired(annotation.required());
        setType(annotation.type());
        setOrgId(orgId);
        setResourceType(annotation.resourceType());
        setResourceId(resourceId);
        setPrivileges(annotation.privileges());
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public UserPrivilege[] getPrivileges() {
        return privileges;
    }

    @JsonIgnore
    public Set<UserPrivilege> getPrivilegeSet() {
        return privileges == null
            ? new HashSet<>()
            : new HashSet<>(Arrays.asList(privileges));
    }

    @JsonIgnore
    public Set<String> getPrivilegeCodes() {

        Set<String> privilegeCodes = new HashSet<>();

        if (privileges == null) {
            return privilegeCodes;
        }

        for (UserPrivilege userPrivilege : privileges) {
            privilegeCodes.add(userPrivilege.toString());
        }

        return privilegeCodes;
    }

    public void setPrivileges(UserPrivilege[] privileges) {
        this.privileges = privileges;
    }

    public boolean isPrivilegeRequired() {

        Set<UserPrivilege> privileges = getPrivilegeSet();

        return !(
            privileges == null
                || privileges.size() == 0
                || (privileges.size() == 1 && privileges.contains(UserPrivilege.NONE))
        );

    }

}
