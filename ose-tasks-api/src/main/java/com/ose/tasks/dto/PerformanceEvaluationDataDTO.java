package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;


public class PerformanceEvaluationDataDTO extends BaseDTO {

    private static final long serialVersionUID = 7785500557399672402L;
    @Schema(description = "用户id")
    private Long engineerId;
    @Schema(description = "用户名")
    private String employeeId;

    @Schema(description = "用户姓名")
    private String displayName;

    @Schema(description = "公司")
    private String company;

    @Schema(description = "部门")
    private String division;

    @Schema(description = "")
    private String team;

    @Schema(description = "")
    private String title;

    @Schema(description = "参与项目数量")
    private Integer projectQty;

    @Schema(description = "参与项目")
    private String projectList;

    @Schema(description = "假期工时")
    private Double holiday;

    @Schema(description = "项目为Leave的工时数（不含holiday）")
    private Double totalLeave;

    @Schema(description = "非overtime工时")
    private Double totalNormalManHour;

    @Schema(description = "overtime工时")
    private Double totalOvertime;

    @Schema(description = "项目工时数(包括周末和节假日)")
    private Double projectManhour;

    @Schema(description = "Non-project Task中不为Leave的工时数")
    private Double nonProjectHour;

    @Schema(description = "标准工时")
    private Double standardHour;

    @Schema(description = "饱和度")
    private Double pjManhourSaturation;

    @Schema(description = "饱和度（除去Training和study）")
    private Double pjManhourSaturationStudyExclude;

    @Schema(description = "正常项目中Task为Training/Study的工时数")
    private Double generalStudyHour;

    @Schema(description = "Non-project Task中General_training/General_study工时数")
    private Double projectStudyHour;

    public Long getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(Long engineerId) {
        this.engineerId = engineerId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getProjectQty() {
        return projectQty;
    }

    public void setProjectQty(Integer projectQty) {
        this.projectQty = projectQty;
    }

    public String getProjectList() {
        return projectList;
    }

    public void setProjectList(String projectList) {
        this.projectList = projectList;
    }

    public Double getHoliday() {
        return holiday;
    }

    public void setHoliday(Double holiday) {
        this.holiday = holiday;
    }

    public Double getTotalLeave() {
        return totalLeave;
    }

    public void setTotalLeave(Double totalLeave) {
        this.totalLeave = totalLeave;
    }

    public Double getTotalNormalManHour() {
        return totalNormalManHour;
    }

    public void setTotalNormalManHour(Double totalNormalManHour) {
        this.totalNormalManHour = totalNormalManHour;
    }

    public Double getTotalOvertime() {
        return totalOvertime;
    }

    public void setTotalOvertime(Double totalOvertime) {
        this.totalOvertime = totalOvertime;
    }

    public Double getProjectManhour() {
        return projectManhour;
    }

    public void setProjectManhour(Double projectManhour) {
        this.projectManhour = projectManhour;
    }

    public Double getNonProjectHour() {
        return nonProjectHour;
    }

    public void setNonProjectHour(Double nonProjectHour) {
        this.nonProjectHour = nonProjectHour;
    }

    public Double getStandardHour() {
        return standardHour;
    }

    public void setStandardHour(Double standardHour) {
        this.standardHour = standardHour;
    }

    public Double getPjManhourSaturation() {
        return pjManhourSaturation;
    }

    public void setPjManhourSaturation(Double pjManhourSaturation) {
        this.pjManhourSaturation = pjManhourSaturation;
    }

    public Double getPjManhourSaturationStudyExclude() {
        return pjManhourSaturationStudyExclude;
    }

    public void setPjManhourSaturationStudyExclude(Double pjManhourSaturationStudyExclude) {
        this.pjManhourSaturationStudyExclude = pjManhourSaturationStudyExclude;
    }

    public Double getGeneralStudyHour() {
        return generalStudyHour;
    }

    public void setGeneralStudyHour(Double generalStudyHour) {
        this.generalStudyHour = generalStudyHour;
    }

    public Double getProjectStudyHour() {
        return projectStudyHour;
    }

    public void setProjectStudyHour(Double projectStudyHour) {
        this.projectStudyHour = projectStudyHour;
    }
}
