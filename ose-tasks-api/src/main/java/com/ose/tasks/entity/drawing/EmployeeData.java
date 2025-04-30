package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * 请假信息
 */
@Entity
@Table(name = "employee_information")
public class EmployeeData  {

    @Id
    private Long serialNo;

    @Schema(description = "工号")
    private String employeeId;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "所属公司")
    private String company;

    @Schema(description = "合同公司")
    private String contractCompany;

    @Schema(description = "所属事业部")
    private String division;

    @Schema(description = "所属部门")
    private String department;

    @Schema(description = "所属团队")
    private String team;

    @Schema(description = "首次参加工作时间")
    private String initialEmploymentDate;

    @Schema(description = "工龄")
    private String lengthOfCareer;

    @Schema(description = "转正时间")
    private String transfer_to_regular_date;

    public Long getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Long serialNo) {
        this.serialNo = serialNo;
    }

    public String getTransfer_to_regular_date() {
        return transfer_to_regular_date;
    }

    public void setTransfer_to_regular_date(String transfer_to_regular_date) {
        this.transfer_to_regular_date = transfer_to_regular_date;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContractCompany() {
        return contractCompany;
    }

    public void setContractCompany(String contractCompany) {
        this.contractCompany = contractCompany;
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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getInitialEmploymentDate() {
        return initialEmploymentDate;
    }

    public void setInitialEmploymentDate(String initialEmploymentDate) {
        this.initialEmploymentDate = initialEmploymentDate;
    }

    public String getLengthOfCareer() {
        return lengthOfCareer;
    }

    public void setLengthOfCareer(String lengthOfCareer) {
        this.lengthOfCareer = lengthOfCareer;
    }
}
