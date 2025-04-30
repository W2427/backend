package com.ose.notifications.dto;

import com.ose.dto.BaseDTO;
import com.ose.notifications.dto.receiver.TeamReceiverDTO;
import com.ose.notifications.dto.receiver.UserReceiverDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

/**
 * 通知发送数据传输对象。
 */
public class NotificationPostDTO extends BaseDTO {

    private static final long serialVersionUID = 7788706009069700249L;

    @Schema(description = "接收者列表")
    private Set<UserReceiverDTO> users;

    @Schema(description = "接收工作组列表")
    private Set<TeamReceiverDTO> teams;

    @Schema(description = "消息参数")
    private Object parameters;

    public Set<UserReceiverDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserReceiverDTO> users) {
        this.users = users;
    }

    public Set<TeamReceiverDTO> getTeams() {
        return teams;
    }

    public void setTeams(Set<TeamReceiverDTO> teams) {
        this.teams = teams;
    }

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }

}
