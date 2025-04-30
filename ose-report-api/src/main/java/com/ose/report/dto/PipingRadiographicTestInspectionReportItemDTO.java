package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PipingRadiographicTestInspectionReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 6791169537595132351L;

    @Schema(name = "焊口号")
    private String weldNo;

    @Schema(name = "焊工号")
    private String welderNo;

    @Schema(name = "材质")
    private String material;

    @Schema(name = "npsX壁厚")
    private String dia;

    @Schema(name = "结果")
    private String result;

    @Schema(name = "焊接日期")
    private String weldDate;

    @Schema(name = "流程id")
    private Long actInstId;

    public String getWeldNo() {
        return weldNo;
    }

    public void setWeldNo(String weldNo) {
        this.weldNo = weldNo;
    }

    public String getWelderNo() {
        return welderNo;
    }

    public void setWelderNo(String welderNo) {
        this.welderNo = welderNo;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getWeldDate() {
        return weldDate;
    }

    public void setWeldDate(String weldDate) {
        this.weldDate = weldDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }
}
