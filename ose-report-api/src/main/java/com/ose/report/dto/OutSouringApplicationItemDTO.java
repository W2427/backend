package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public class OutSouringApplicationItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -1996934265438337472L;

    @Schema(description = "构件名称")
    private String companentName;

    @Schema(description = "规格型号")
    private String specification;

    @Schema(description = "图号")
    private String subsectionNo;

    @Schema(description = "构件号")
    private String componentNo;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "数量")
    private String quantity;

    @Schema(description = "单重")
    private String weight;

    @Schema(description = "总重")
    private String totalWeight;

    @Schema(description = "加工类型")
    private String process;

    @Schema(description = "需用日期")
    private Date requiredDate;

    @Schema(description = "备注")
    private String remark;

    public String getCompanentName() {
        return companentName;
    }

    public void setCompanentName(String companentName) {
        this.companentName = companentName;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getSubsectionNo() {
        return subsectionNo;
    }

    public void setSubsectionNo(String subsectionNo) {
        this.subsectionNo = subsectionNo;
    }

    public String getComponentNo() {
        return componentNo;
    }

    public void setComponentNo(String componentNo) {
        this.componentNo = componentNo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Date getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(Date requiredDate) {
        this.requiredDate = requiredDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}


