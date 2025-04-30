package com.ose.tasks.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;


public class ManHourDetailDTO extends PageDTO {

    private static final long serialVersionUID = 3388642907990208196L;

    @Schema(description = "项目")
    private String projectName;

    @Schema(description = "图纸编号")
    private String drawingNo;

    @Schema(description = "图纸标题")
    private String documentTitle;

    @Schema(description = "阶段")
    private String stage;

    @Schema(description = "版本")
    private String rev;

    @Schema(description = "任务")
    private String task;

    @Schema(description = "流程")
    private String activity;

    @Schema(description = "专业")
    private String discipline;

    @Schema(description = "执行人")
    private String assignedBy;

    @Schema(description = "开始日期")
    private String startDate;

    @Schema(description = "结束日期")
    private String endDate;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
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

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
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

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
