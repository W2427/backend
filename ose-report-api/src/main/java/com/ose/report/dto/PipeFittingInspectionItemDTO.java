package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public class PipeFittingInspectionItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 6246861725194768652L;

    @Schema(description = "编号")
    private String sn;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "材质")
    private String grade;

    @Schema(description = "规格")
    private String size;

    @Schema(description = "数量")
    private String qty;

    @Schema(description = "炉号")
    private String heatNo;

    @Schema(description = "批量号")
    private String batchNo;

    @Schema(description = "生产厂家")
    private String manufacturer;

    @Schema(description = "证书号")
    private String certificationNo;

    @Schema(description = "结果")
    private String result;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCertificationNo() {
        return certificationNo;
    }

    public void setCertificationNo(String certificationNo) {
        this.certificationNo = certificationNo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
