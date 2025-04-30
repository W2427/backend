package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Date;

public class DocumentTransmittalRecordDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -1813291472641374038L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "传送单编号")
    private String transmittalNo;

    @Schema(description = "项目编号")
    private String projectNo;

    @Schema(description = "发放部门")
    private String department;

    @Schema(description = "发放日期")
    private Date date;

    @Schema(description = "所属专业")
    private String discipline;

    private List<DocumentTransmittalItemDTO> items;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTransmittalNo() {
        return transmittalNo;
    }

    public void setTransmittalNo(String transmittalNo) {
        this.transmittalNo = transmittalNo;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    @Override
    public List<DocumentTransmittalItemDTO> getItems() {
        return items;
    }

    public void setItems(List<DocumentTransmittalItemDTO> items) {
        this.items = items;
    }
}
