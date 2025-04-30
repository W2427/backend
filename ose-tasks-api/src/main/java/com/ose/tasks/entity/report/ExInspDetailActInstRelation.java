package com.ose.tasks.entity.report;

import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 外检申请DETAIL 和 流程proc_inst_id 的关系表
 */
@Entity
@Table(name = "bpm_exinsp_detail_actinst_relation",
indexes = {
    @Index(columnList = "orgId,projectId,exInspScheduleDetailId,actInstId"),
    @Index(columnList = "actInstId")
})
public class ExInspDetailActInstRelation extends BaseBizEntity {

    private static final long serialVersionUID = 3114713144791716231L;
    @Schema(description = "ORG ID")
    @Column
    private Long orgId;

    @Schema(description = "PROJECT ID")
    @Column
    private Long projectId;

    @Schema(description = "外检申请 编号")
    @Column
    private Long exInspScheduleDetailId;

    @Schema(description = "流程 act_inst_id")
    @Column
    private Long actInstId;

    @Schema(description = "任务 actTaskId")
    @Column
    private String actTaskId;

    @Schema(description = "ExSchedule的 actTaskId")
    @Column
    private String actTaskIdOnSchedule;

    @Schema(description = "项目节点 PN ID")
    @Column
    private Long projectNodeId;

    @Schema(description = "REPORT ID")
    @Column
    private Long reportId;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getExInspScheduleDetailId() {
        return exInspScheduleDetailId;
    }

    public void setExInspScheduleDetailId(Long exInspScheduleDetailId) {
        this.exInspScheduleDetailId = exInspScheduleDetailId;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getActTaskId() {
        return actTaskId;
    }

    public void setActTaskId(String actTaskId) {
        this.actTaskId = actTaskId;
    }

    public Long getProjectNodeId() {
        return projectNodeId;
    }

    public void setProjectNodeId(Long projectNodeId) {
        this.projectNodeId = projectNodeId;
    }

    public String getActTaskIdOnSchedule() {
        return actTaskIdOnSchedule;
    }

    public void setActTaskIdOnSchedule(String actTaskIdOnSchedule) {
        this.actTaskIdOnSchedule = actTaskIdOnSchedule;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }
}
