package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class MaterialReturnStockDTO extends BaseListReportDTO {

    private static final long serialVersionUID = 5485933996125120195L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "使用日期")
    private Date date;

    @Schema(description = "领料单编号")
    private String materialApplicationNo;

    @Schema(description = "退库编号")
    private String materialReturnNo;

    @Schema(description = "出库单号")
    private String materialIssueNo;

    @Schema(description = "分包商")
    private String subContractor;
    private List<MaterialReturnItemDTO> items;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMaterialApplicationNo() {
        return materialApplicationNo;
    }

    public void setMaterialApplicationNo(String materialApplicationNo) {
        this.materialApplicationNo = materialApplicationNo;
    }

    public String getMaterialReturnNo() {
        return materialReturnNo;
    }

    public void setMaterialReturnNo(String materialReturnNo) {
        this.materialReturnNo = materialReturnNo;
    }

    public String getMaterialIssueNo() {
        return materialIssueNo;
    }

    public void setMaterialIssueNo(String materialIssueNo) {
        this.materialIssueNo = materialIssueNo;
    }

    public String getSubContractor() {
        return subContractor;
    }

    public void setSubContractor(String subContractor) {
        this.subContractor = subContractor;
    }

    @Override
    public List<MaterialReturnItemDTO> getItems() {
        return items;
    }

    public void setItems(List<MaterialReturnItemDTO> items) {
        this.items = items;
    }
}
