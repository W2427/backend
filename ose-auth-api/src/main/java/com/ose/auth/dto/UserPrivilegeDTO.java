package com.ose.auth.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

public class UserPrivilegeDTO extends BaseDTO {

    private static final long serialVersionUID = 6926845556193693055L;

    public static final String ALL_PRIVILEGE_VALUE = "all";

    @Schema(description = "组织 ID")
    private Long orgId;

    @Schema(description = "权限列表")
    private Set<String> privileges = new HashSet<>();

    public UserPrivilegeDTO() {
    }

    public UserPrivilegeDTO(Long orgId, Set<String> privileges) {
        setOrgId(orgId);
        setPrivileges(privileges);
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Set<String> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String privileges) {
        setPrivileges(privileges == null ? new HashSet<>() : new HashSet<>(Arrays.asList(privileges.split(","))));
    }

    @JsonSetter
    public void setPrivileges(Set<String> privileges) {

        if (privileges == null) {
            privileges = new HashSet<>();
        }

        if (privileges.contains(ALL_PRIVILEGE_VALUE)) {
            privileges.clear();
            privileges.add(ALL_PRIVILEGE_VALUE);
        }

        privileges.remove("");

        this.privileges = privileges;
    }

    /**
     * 将用户的【组织-权限】的列表转换为【组织-权限集合】的映射表。
     *
     * @param userPrivileges 【组织-权限】列表
     * @return 【组织-权限集合】映射表
     */
    public static Map<Long, Set<String>> toMap(List<UserPrivilegeDTO> userPrivileges) {

        Map<Long, Set<String>> userPrivilegeMap = new HashMap<>();

        if (userPrivileges == null || userPrivileges.size() == 0) {
            return userPrivilegeMap;
        }

        Set<String> privileges;

        for (UserPrivilegeDTO userPrivilege : userPrivileges) {

            privileges = userPrivilegeMap.computeIfAbsent(
                userPrivilege.getOrgId(),
                key -> new HashSet<>()
            );

            privileges.addAll(userPrivilege.getPrivileges());

            if (privileges.contains(ALL_PRIVILEGE_VALUE)) {
                privileges.clear();
                privileges.add(ALL_PRIVILEGE_VALUE);
            }

        }

        return userPrivilegeMap;
    }

}
