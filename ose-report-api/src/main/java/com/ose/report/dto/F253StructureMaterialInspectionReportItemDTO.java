package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class F253StructureMaterialInspectionReportItemDTO extends BaseReportListItemDTO{

    private static final long serialVersionUID = 4600958013578237845L;

    @Schema(description = "编号")
    private String no;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "材质")
    private String grade;

    @Schema(description = "规格")
    private String size;

    @Schema(description = "数量")
    private String qty;

    @Schema(description = "炉号")
    private String plateNo;

    @Schema(description = "炉批号")
    private String heatBatchNo;

    @Schema(description = "生产厂家")
    private String manufacturer;

    @Schema(description = "证书号")
    private String certificationNo;

    @Schema(description = "结果")
    private String result;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getHeatBatchNo() {
        return heatBatchNo;
    }

    public void setHeatBatchNo(String heatBatchNo) {
        this.heatBatchNo = heatBatchNo;
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
