package com.ose.tasks.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class OverTimeApplicationFormSearchDTO extends PageDTO {

    private static final long serialVersionUID = -8379942498068456286L;

    private String keyword;

    private String applyStatus;

    private String reviewStatus;

    private String userType;

    private String projectName;

    private String task;

    private String startDate;

    private String company;

    private String department;

    private String division;

    private String team;

    @JsonIgnore
    @Schema(hidden = true)
    private Date startDateTime;

    private String endDate;

    @JsonIgnore
    @Schema(hidden = true)
    private Date endDateTime;

    private Boolean reviewRole;

    private Boolean applyRole;

    private Boolean teamLeader;

    private Boolean divisionVP;

    private Boolean companyGM;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
        this.startDateTime = parseDate(startDate);
    }

    private Date parseDate(String dateStr) {
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return f.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
        this.endDateTime = parseDate(endDate);
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Boolean getReviewRole() {
        return reviewRole;
    }

    public void setReviewRole(Boolean reviewRole) {
        this.reviewRole = reviewRole;
    }

    public Boolean getApplyRole() {
        return applyRole;
    }

    public void setApplyRole(Boolean applyRole) {
        this.applyRole = applyRole;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Boolean getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(Boolean teamLeader) {
        this.teamLeader = teamLeader;
    }

    public Boolean getDivisionVP() {
        return divisionVP;
    }

    public void setDivisionVP(Boolean divisionVP) {
        this.divisionVP = divisionVP;
    }

    public Boolean getCompanyGM() {
        return companyGM;
    }

    public void setCompanyGM(Boolean companyGM) {
        this.companyGM = companyGM;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
