package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PipingMaterialReceiveItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -1822930255167295716L;

    @Schema(description = "材料编码")
    private String poItem;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "材质")
    private String grade;

    @Schema(description = "炉批号")
    private String heatNo;

    @Schema(description = "生产厂家")
    private String supplier;


    public String getPoItem() {
        return poItem;
    }

    public void setPoItem(String poItem) {
        this.poItem = poItem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
