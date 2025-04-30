package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class F253WeldingConsumableReceivingInspectionReportItemDTO {

    private static final long serialVersionUID = 3106525209009452414L;

    @Schema(name = "编号")
    private String no;

    @Schema(name = "名称")
    private String name;

    @Schema(name = "规格")
    private String size;

    @Schema(name = "批号")
    private String batchNo;

    @Schema(name = "数量")
    private String qty;

    @Schema(name = "生产厂家")
    private String manufacturer;

    @Schema(name = "证明书")
    private String certificationNo;

    @Schema(name = "结果")
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
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
