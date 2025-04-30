package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;


public class ReviewDataDTO extends BaseDTO {

    private static final long serialVersionUID = 5170133670356434949L;
    @Schema(description = "用户姓名")
    private String username;

    @Schema(description = "公司")
    private String company;

    @Schema(description = "部门")
    private String division;

    @Schema(description = "部门")
    private String department;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "工程师名称")
    private String engineer;

    @Schema(description = "版本")
    private String rev;

    @Schema(description = "任务")
    private String task;

    @Schema(description = "流程")
    private String activity;

    @Schema(description = "分配人")
    private String assignedBy;

    @Schema(description = "填报日期")
    private String workHourDate;

    @Schema(description = "正常工时")
    private Double workHour;

    @Schema(description = "加班工时")
    private Double outHour;

    @Schema(description = "阶段")
    private String stage;

    @Schema(description = "文件标题")
    private String documentTitle;

    @Schema(description = "文件编号")
    private String drawingNo;

    @Schema(description = "专业")
    private String discipline;

    @Schema(description = "备注")
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getEngineer() {
        return engineer;
    }

    public void setEngineer(String engineer) {
        this.engineer = engineer;
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

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
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

    public Double getOutHour() {
        return outHour;
    }

    public void setOutHour(Double outHour) {
        this.outHour = outHour;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
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
}
