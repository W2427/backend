package com.ose.tasks.dto;
import com.ose.dto.PageDTO;

import java.util.List;


public class PerformanceEvaluationSearchDTO extends PageDTO {


    private static final long serialVersionUID = 7854537578782958416L;
    private String keyword;

    private String startDate;

    private String endDate;

    private String projectName;

    private String company;

    private String division;

    private String team;

    private String title;

    private List<String> holidayDates;

    private String userType;

    private String teamLeader;

    private String divisionVP;

    private String companyGM;

    private String reviewCompany;

    private String reviewCompany2;

    public String getReviewCompany() {
        return reviewCompany;
    }

    public void setReviewCompany(String reviewCompany) {
        this.reviewCompany = reviewCompany;
    }

    public String getReviewCompany2() {
        return reviewCompany2;
    }

    public void setReviewCompany2(String reviewCompany2) {
        this.reviewCompany2 = reviewCompany2;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(String teamLeader) {
        this.teamLeader = teamLeader;
    }

    public String getDivisionVP() {
        return divisionVP;
    }

    public void setDivisionVP(String divisionVP) {
        this.divisionVP = divisionVP;
    }

    public String getCompanyGM() {
        return companyGM;
    }

    public void setCompanyGM(String companyGM) {
        this.companyGM = companyGM;
    }

    public List<String> getHolidayDates() {
        return holidayDates;
    }

    public void setHolidayDates(List<String> holidayDates) {
        this.holidayDates = holidayDates;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
