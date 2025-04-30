package com.ose.tasks.dto;

import java.util.List;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 项目 WBS 计划三级跟四级
 */
public class WBSEntryDelegateDTO extends BaseDTO {

    private static final long serialVersionUID = 3753812239306672043L;

    @Schema(description = "工作组 ID")
    private Long teamId;

    @Schema(description = "工作组 名称")
    private String teamName;

    @Schema(description = "用户权限")
    private List<WBSEntryUserDelegateDTO> userPrivileges;

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

    public List<WBSEntryUserDelegateDTO> getUserPrivileges() {
        return userPrivileges;
    }

    public void setUserPrivileges(List<WBSEntryUserDelegateDTO> userPrivileges) {
        this.userPrivileges = userPrivileges;
    }

}
