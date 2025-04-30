package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.*;

/**
 * 工作组-权限数据传输对象。
 */
public class TeamPrivilegeListDTO extends BaseDTO {

    private static final long serialVersionUID = -2330901159759651831L;

    @Schema(description = "工作组列表")
    @NotNull
    @Size(min = 1)
    private List<TeamPrivilegeDTO> teams = new ArrayList<>();

    public TeamPrivilegeListDTO() {
        super();
    }

    public TeamPrivilegeListDTO(List<TeamPrivilegeDTO> teams) {
        this.teams = teams;
    }

    public List<TeamPrivilegeDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamPrivilegeDTO> teams) {
        this.teams = teams;
    }

    public Map<Long, Set<String>> toMap() {

        Map<Long, Set<String>> map = new HashMap<>();

        for (TeamPrivilegeInterface team : teams) {
            map.put(team.getTeamId(), team.getMemberPrivileges());
        }

        return map;
    }

}
