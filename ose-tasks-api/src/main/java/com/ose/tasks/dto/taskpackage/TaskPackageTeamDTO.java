package com.ose.tasks.dto.taskpackage;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 任务包更新数据传输对象。
 */
public class TaskPackageTeamDTO extends BaseDTO {

    private static final long serialVersionUID = -1750770602876909258L;

    @Valid
    private List<TeamDTO> teams = null;

    public List<TeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamDTO> teams) {
        this.teams = teams;
    }

    public static class TeamDTO implements Serializable {

        private static final long serialVersionUID = 4500123386736447350L;

        @Schema(description = "工序 ID 或【工序阶段名称/工序名称】")
        private Long processId;

        @Schema(description = "工作组 ID")
        private Long teamId;

        @Schema(description = "工作场地 ID")
        private Long workSiteId;

        @Schema(description = "计划开始时间")
        private Date planStartDate;

        @Schema(description = "计划结束暗井")
        private Date planEndDate;

        @Schema(description = "计划工时")
        private Double planHours;

        public Long getProcessId() {
            return processId;
        }

        public void setProcessId(Long processId) {
            this.processId = processId;
        }

        public Long getTeamId() {
            return teamId;
        }

        public void setTeamId(Long teamId) {
            this.teamId = teamId;
        }

        public Long getWorkSiteId() {
            return workSiteId;
        }

        public void setWorkSiteId(Long workSiteId) {
            this.workSiteId = workSiteId;
        }

        public Date getPlanStartDate() {
            return planStartDate;
        }

        public void setPlanStartDate(Date planStartDate) {
            this.planStartDate = planStartDate;
        }

        public Date getPlanEndDate() {
            return planEndDate;
        }

        public void setPlanEndDate(Date planEndDate) {
            this.planEndDate = planEndDate;
        }

        public Double getPlanHours() {
            return planHours;
        }

        public void setPlanHours(Double planHours) {
            this.planHours = planHours;
        }
    }
}
