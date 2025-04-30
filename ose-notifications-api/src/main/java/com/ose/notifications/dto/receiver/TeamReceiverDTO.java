package com.ose.notifications.dto.receiver;

import com.ose.auth.dto.TeamPrivilegeInterface;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * 消息接收者数据传输对象。
 */
public class TeamReceiverDTO extends ReceiverDTO implements TeamPrivilegeInterface {

    private static final long serialVersionUID = 6321924856977153200L;

    @Schema(description = "工作组 ID")
    @NotEmpty
    private Long teamId;

    @Schema(description = "用户在工作组所拥有的权限")
    private Set<String> memberPrivileges;

    @Override
    public Long getTeamId() {
        return teamId;
    }

    @Override
    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    @Override
    public Set<String> getMemberPrivileges() {
        return memberPrivileges;
    }

    @Override
    public void setMemberPrivileges(Set<String> memberPrivileges) {
        this.memberPrivileges = memberPrivileges;
    }

}
