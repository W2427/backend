package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


public class AttendanceFilterDTO extends BaseDTO {

    private static final long serialVersionUID = 9023154567613866118L;

    @Schema(description = "一级部门")
    private List<String> divisionList;

    @Schema(description = "一级部门")
    private List<String> teamList;

    @Schema(description = "二级部门")
    private List<String> departmentList;

    @Schema(description = "公司")
    private List<String> companyList;

    @Schema(description = "周次")
    private List<Integer> weeklyList;

    @Schema(description = "")
    private List<String> titleList;

    public List<String> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<String> titleList) {
        this.titleList = titleList;
    }

    public List<Integer> getWeeklyList() {
        return weeklyList;
    }

    public void setWeeklyList(List<Integer> weeklyList) {
        this.weeklyList = weeklyList;
    }

    public List<String> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(List<String> divisionList) {
        this.divisionList = divisionList;
    }

    public List<String> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<String> departmentList) {
        this.departmentList = departmentList;
    }

    public List<String> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<String> companyList) {
        this.companyList = companyList;
    }

    public List<String> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<String> teamList) {
        this.teamList = teamList;
    }
}
