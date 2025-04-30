package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.vo.DrawingRecordStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;


/**
 * 图纸记录
 */
@Entity
@Table(name = "drawing_record",
    indexes = {
        @Index(columnList = "drawingId,engineerId")
    })
public class DrawingRecord extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 27372150972503750L;

    @Schema(description = "组织Id")
    private Long orgId;

    @Schema(description = "项目Id")
    private Long projectId;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "图纸Id")
    private Long drawingId;

    @Schema(description = "工程师Id")
    private Long engineerId;

    @Schema(description = "工程师名称")
    private String engineer;

    @Schema(description = "版本")
    private String rev;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "任务")
    private String task;

    @Schema(description = "流程")
    private String activity;

    @Schema(description = "分配人")
    private String assignedBy;

    @Schema(description = "填报日期")
    private String workHourDate;

    @Schema(description = "正常工时")
    private Double workHour = 0.0;

    @Schema(description = "加班工时")
    private Double outHour = 0.0;

    @Schema(description = "工时记录状态")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private DrawingRecordStatus drawingRecordStatus;

    @Schema(description = "阶段")
    private String stage;

    @Schema(description = "阶段Id")
    private Long stageId;

    @Schema(description = "文件标题")
    private String documentTitle;

    @Schema(description = "文件编号")
    private String drawingNo;

    @Schema(description = "功能块")
    private String funcPart;

    @Schema(description = "编辑状态")
    private Boolean editStatus = false;

    @Schema(description = "周次")
    private Integer weekly;

    @Schema(description = "月次")
    private Integer monthly;

    @Schema(description = "正常工时")
    private Double overManHour = 0.0;

    @Schema(description = "正常工时限制")
    private Double maxWorkHour ;

    public Integer getMonthly() {
        return monthly;
    }

    public void setMonthly(Integer monthly) {
        this.monthly = monthly;
    }

    public Double getMaxWorkHour() {
        return maxWorkHour;
    }

    public void setMaxWorkHour(Double maxWorkHour) {
        this.maxWorkHour = maxWorkHour;
    }

    public Double getOverManHour() {
        return overManHour;
    }

    public void setOverManHour(Double overManHour) {
        this.overManHour = overManHour;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

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

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public Long getEngineerId() {
        return engineerId;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public void setEngineerId(Long engineerId) {
        this.engineerId = engineerId;
    }

    public String getEngineer() {
        return engineer;
    }

    public void setEngineer(String engineer) {
        this.engineer = engineer;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getWorkHourDate() {
        return workHourDate;
    }

    public void setWorkHourDate(String workHourDate) {
        this.workHourDate = workHourDate;
    }

    public Double getWorkHour() {
        return workHour;
    }

    public void setWorkHour(Double workHour) {
        this.workHour = workHour;
    }

    public DrawingRecordStatus getDrawingRecordStatus() {
        return drawingRecordStatus;
    }

    public void setDrawingRecordStatus(DrawingRecordStatus drawingRecordStatus) {
        this.drawingRecordStatus = drawingRecordStatus;
    }

    public Double getOutHour() {
        return outHour;
    }

    public void setOutHour(Double outHour) {
        this.outHour = outHour;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }

    public Boolean getEditStatus() {
        return editStatus;
    }

    public void setEditStatus(Boolean editStatus) {
        this.editStatus = editStatus;
    }

    public Integer getWeekly() {
        return weekly;
    }

    public void setWeekly(Integer weekly) {
        this.weekly = weekly;
    }
}
