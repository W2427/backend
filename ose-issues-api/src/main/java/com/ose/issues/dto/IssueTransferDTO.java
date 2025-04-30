package com.ose.issues.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class IssueTransferDTO extends BaseDTO {

    private static final long serialVersionUID = 8643658444387411371L;

    @Schema(description = "负责人")
    private Long personInCharge;

    @Schema(description = "QC")
    private Long qc;

    @Schema(description = "移交原因")
    private String comment;

    @Schema(description = "部门Id")
    private List<Long> departments;

    @Schema(description = "问题列表")
    @NotEmpty
    private List<Long> issues;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(Long personInCharge) {
        this.personInCharge = personInCharge;
    }

    public List<Long> getIssues() {
        return issues;
    }

    public void setIssues(List<Long> issues) {
        this.issues = issues;
    }

    public Long getQc() {
        return qc;
    }

    public void setQc(Long qc) {
        this.qc = qc;
    }

    public List<Long> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Long> departments) {
        this.departments = departments;
    }
}
