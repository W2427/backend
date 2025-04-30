package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public class ValveInspectionItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 4844776989547787677L;

    @Schema(description = "编号")
    private String sn;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "规格")
    private String size;

    @Schema(description = "规格描述")
    private String description;

    @Schema(description = "位号")
    private String tagNo;

    @Schema(description = "数量")
    private String qty;

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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTagNo() {
        return tagNo;
    }

    public void setTagNo(String tagNo) {
        this.tagNo = tagNo;
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
