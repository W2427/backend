package com.ose.tasks.dto;

import com.ose.auth.vo.OrgType;
import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class ReviewCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -1062697674875084981L;
    @Schema(description = "专业")
    private String discipline;
    @Schema(description = "周次")
    private Integer weekly;
    @Schema(description = "公司")
    private String company;

    @Schema(description = "一级部门")
    private String division;

    @Schema(description = "二级部门")
    private String department;

    @Schema(description = "一级部门")
    private String team;

    @Schema(description = "开始日期")
    private String startDate;

    @Schema(description = "结束日期")
    private String endDate;
    @Schema(description = "名称")
    private String username;
    @Schema(description = "项目名")
    private String projectName;
    @Schema(description = "公司")
    private String company2;

    @Schema(description = "一级部门")
    private String division2;

    @Schema(description = "组织类型")
    private String orgType;

    @Schema(description = "组织类型")
    private Long orgId;

    private Long userId;

    private List<Long> userIds;

    private List<Long> orgIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(List<Long> orgIds) {
        this.orgIds = orgIds;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getCompany2() {
        return company2;
    }

    public void setCompany2(String company2) {
        this.company2 = company2;
    }

    public String getDivision2() {
        return division2;
    }

    public void setDivision2(String division2) {
        this.division2 = division2;
    }

    private Boolean teamLeader;

    private Boolean divisionVP;

    private Boolean companyGM;

    private Boolean admin;

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
