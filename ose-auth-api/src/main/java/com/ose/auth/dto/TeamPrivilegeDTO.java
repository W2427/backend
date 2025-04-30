package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import com.ose.util.BeanUtils;

import java.util.Set;

/**
 * 工作组-权限数据传输对象。
 */
public class TeamPrivilegeDTO extends BaseDTO implements TeamPrivilegeInterface {

    private static final long serialVersionUID = -7720356335773951586L;

    private Long teamId;

    private Set<String> memberPrivileges;

    public TeamPrivilegeDTO() {
        super();
    }

    public TeamPrivilegeDTO(TeamPrivilegeInterface teamPrivilege) {
        BeanUtils.copyProperties(teamPrivilege, this);
    }

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
