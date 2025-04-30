package com.ose.tasks.dto;

import java.util.List;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 项目 WBS 三级计划四级计划指定人员
 */
public class WBSEntryUserDelegateDTO extends BaseDTO {

    private static final long serialVersionUID = 3753812239306672043L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作组 ID")
    private Long teamId;

    @Schema(description = "工作组 名称")
    private String teamName;

    @Schema(description = "用户ID")
    private List<Long> userIds;

    @Schema(description = "用户名称")
    private List<String> userNames;

    @Schema(description = "权限")
    @Enumerated(EnumType.STRING)
    private UserPrivilege privilege;

    @Schema(description = "权限名称")
    private String privilegeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public UserPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(UserPrivilege privilege) {
        this.privilege = privilege;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public List<String> getUserName() {
        return userNames;
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

}
