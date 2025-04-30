package com.ose.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

/**
 * 工作组-权限数据传输对象。
 */
public interface TeamPrivilegeInterface {

    @Schema(description = "工作组 ID")
    Long getTeamId();

    void setTeamId(Long teamId);

    @Schema(description = "权限集合")
    Set<String> getMemberPrivileges();

    void setMemberPrivileges(Set<String> privileges);

}
