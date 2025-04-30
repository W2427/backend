package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 传输对象
 */
public class OverTimeApplicationFormFilterDTO extends BaseDTO {
    private static final long serialVersionUID = 4534993125074569974L;
    @Schema(description = "一级部门")
    private List<String> teamList;

    @Schema(description = "二级部门")
    private List<String> departmentList;

    @Schema(description = "二级部门")
    private List<String> divisionList;

    @Schema(description = "公司")
    private List<String> companyList;

    @Schema(description = "项目")
    private List<String> projectNameList;

    public List<String> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<String> teamList) {
        this.teamList = teamList;
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

    public List<String> getProjectNameList() {
        return projectNameList;
    }

    public void setProjectNameList(List<String> projectNameList) {
        this.projectNameList = projectNameList;
    }

    public List<String> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(List<String> divisionList) {
        this.divisionList = divisionList;
    }
}
