package com.ose.tasks.dto.performance;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class PerformanceAppraisalListDTO extends BaseDTO {
    @Schema(description = "员工编号")
    private Long id;

    @Schema(description = "员工编号")
    private String employeeId;

    @Schema(description = "员工姓名")
    private String employeeName;

    @Schema(description = "入职日期")
    private String joiningDate;

    @Schema(description = "转为正式员工日期")
    private String transferToRegularDate;

    @Schema(description = "最高学历")
    private String firstDegree;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getTransferToRegularDate() {
        return transferToRegularDate;
    }

    public void setTransferToRegularDate(String transferToRegularDate) {
        this.transferToRegularDate = transferToRegularDate;
    }

    public String getFirstDegree() {
        return firstDegree;
    }

    public void setFirstDegree(String firstDegree) {
        this.firstDegree = firstDegree;
    }
}
