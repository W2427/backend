package com.ose.tasks.dto.taskpackage;

import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 任务包工序任务分配信息数据传输对象。
 */
public class TaskPackageProcessDelegateDTO extends BaseDTO {

    private static final long serialVersionUID = -5282658862479745321L;

    @Schema(description = "工序 ID")
    private Long id;

    @Schema(description = "项目 ID")
    private Long projectId;

    @Schema(description = "工序阶段名称")
    private String stageName;

    @Schema(description = "工序名称")
    private String processName;

    @Schema(description = "权限")
    private UserPrivilege privilege;

    @Schema(description = "工作组 ID")
    private Long teamId;

    @Schema(description = "工作组名称")
    private String teamName;

    @Schema(description = "用户 ID")
    private Long userId;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "计划任务数量")
    private Long wbsCount;

    @Schema(description = "已完成计划任务数量")
    private Long wbsApprovedCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public UserPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(UserPrivilege privilege) {
        this.privilege = privilege;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getWbsCount() {
        return wbsCount;
    }

    public void setWbsCount(Long wbsCount) {
        this.wbsCount = wbsCount;
    }

    public Long getWbsApprovedCount() {
        return wbsApprovedCount;
    }

    public void setWbsApprovedCount(Long wbsApprovedCount) {
        this.wbsApprovedCount = wbsApprovedCount;
    }
}

