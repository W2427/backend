package com.ose.prints.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 上传文件的附加信息。
 */
public class CuttingDetailDTO extends BaseDTO {


    private static final long serialVersionUID = 8194574818310660466L;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "项目名")
    private String project;

    @Schema(description = "识别码")
    private String ident;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "数量")
    private String qty;

    @Schema(description = "炉批号")
    private String heatNo;

    @Schema(description = "规格")
    private String spec;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "二维码")
    private String qrCode;

    @Schema(description = "描述")
    private String desc;

    @Schema(description = "高度")
    private String height;

    @Schema(description = "打印次数")
    private Integer printCount;

    @Schema(description = "批次")
    private String materialBatchNumber;

    @Schema(description = "图纸简码")
    private String shortCode;

    /**
     * 构造方法。
     */
    public CuttingDetailDTO() {
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Integer getPrintCount() {
        return printCount;
    }

    public void setPrintCount(Integer printCount) {
        this.printCount = printCount;
    }

    public String getMaterialBatchNumber() {
        return materialBatchNumber;
    }

    public void setMaterialBatchNumber(String materialBatchNumber) {
        this.materialBatchNumber = materialBatchNumber;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
}
