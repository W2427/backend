package com.ose.issues.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class IssueDepartmentDTO extends ExperienceUpdateDTO {

    private static final long serialVersionUID = 5525504863323681950L;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "部门id")
    private Long departmentId;

    public IssueDepartmentDTO() {
    }

    public IssueDepartmentDTO(String departmentName, Long departmentId) {
        super();
        this.departmentName = departmentName;
        this.departmentId = departmentId;
    }


    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

}
