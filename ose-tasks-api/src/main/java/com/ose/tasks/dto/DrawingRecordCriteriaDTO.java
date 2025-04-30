package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * @author: DaiZeFeng
 * @date: 2023/2/20
 */
public class DrawingRecordCriteriaDTO extends BaseDTO {
    private static final long serialVersionUID = 1904136931946265840L;

    @Schema(description = "图纸Id")
    private Long drawingId;

    @Schema(description = "工程师Id")
    private Long engineerId;

    @Schema(description = "工程师名称")
    private String engineer;

    @Schema(description = "填报时间")
    private Date workHourDate;

    @Schema(description = "wxapp填报时间")
    private String wxWorkHourDate;

    @Schema(description = "工时")
    private Double workHour;

    @Schema(description = "加班工时")
    private Double outHour;

    @Schema(description = "阶段")
    private String stage;

    @Schema(description = "阶段Id")
    private Long stageId;

    @Schema(description = "工序")
    private String process;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "图纸标题")
    private String documentTitle;

    @Schema(description = "图纸编号")
    private String drawingNo;

    @Schema(description = "版本")
    private String rev;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "任务")
    private String task;

    @Schema(description = "流程")
    private String activity;

    @Schema(description = "填报日期")
    private Date date;

    @Schema(description = "分配人")
    private String assignedBy;

    @Schema(description = "专业")
    private String discipline;

    @Schema(description = "周次")
    private Integer weekly;

    @Schema(description = "申请加班工时")
    private Double overManHour = 0.0;

    @Schema(description = "正常工时限制")
    private Double maxWorkHour ;

    @Schema(description = "是否是自动填写")
    private Boolean isAuto ;

    public Boolean getAuto() {
        return isAuto;
    }

    public void setAuto(Boolean auto) {
        isAuto = auto;
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

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
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

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Double getOutHour() {
        return outHour;
    }

    public void setOutHour(Double outHour) {
        this.outHour = outHour;
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

    public void setEngineerId(Long engineerId) {
        this.engineerId = engineerId;
    }

    public String getEngineer() {
        return engineer;
    }

    public void setEngineer(String engineer) {
        this.engineer = engineer;
    }

    public Date getWorkHourDate() {
        return workHourDate;
    }

    public void setWorkHourDate(Date workHourDate) {
        this.workHourDate = workHourDate;
    }

    public Double getWorkHour() {
        return workHour;
    }

    public void setWorkHour(Double workHour) {
        this.workHour = workHour;
    }

    public String getWxWorkHourDate() {
        return wxWorkHourDate;
    }

    public void setWxWorkHourDate(String wxWorkHourDate) {
        this.wxWorkHourDate = wxWorkHourDate;
    }
    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public Integer getWeekly() {
        return weekly;
    }

    public void setWeekly(Integer weekly) {
        this.weekly = weekly;
    }
}
