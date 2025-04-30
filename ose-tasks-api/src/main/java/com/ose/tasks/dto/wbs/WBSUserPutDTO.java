package com.ose.tasks.dto.wbs;

import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class WBSUserPutDTO extends BaseDTO {

    private static final long serialVersionUID = -6390556179521521152L;

    @Schema(description = "任务分配ID")
    private Long delegateId;

    @Schema(description = "工作组ID")
    private Long teamId = null;

    @Schema(description = "用户ID")
    private Long userId = null;

    @Schema(description = "权限值")
    @Enumerated(EnumType.STRING)
    private UserPrivilege privilege = null;

    public Long getDelegateId() {
        return delegateId;
    }

    public void setDelegateId(Long delegateId) {
        this.delegateId = delegateId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(UserPrivilege privilege) {
        this.privilege = privilege;
    }
}
