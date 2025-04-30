package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class StructureRadiographicTestInspectionReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 4725079811290207486L;

    @Schema(name = "焊口号")
    private String weldNo;

    @Schema(name = "焊工号")
    private String welderNo;

    @Schema(name = "材质")
    private String material;

    @Schema(name = "长度")
    private String weldLength;

    @Schema(name = "壁厚")
    private String thickness;

    @Schema(name = "结果")
    private String result;

    @Schema(name = "流程id")
    private Long actInstId;

    @Schema(name = "")
    private String no;

    @Schema(name = "焊接工序")
    private String weldProcess;

    @Schema(name = "检测日期")
    private String date;

    @Schema(name = "片号")
    private String filmNo;

    @Schema(name = "缺陷类型")
    private String defectType;

    @Schema(name = "缺陷长度")
    private String defectLength;

    @Schema(name = "备注")
    private String remark;

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

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    public String getWeldLength() {
        return weldLength;
    }

    public void setWeldLength(String weldLength) {
        this.weldLength = weldLength;
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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getWeldProcess() {
        return weldProcess;
    }

    public void setWeldProcess(String weldProcess) {
        this.weldProcess = weldProcess;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFilmNo() {
        return filmNo;
    }

    public void setFilmNo(String filmNo) {
        this.filmNo = filmNo;
    }

    public String getDefectType() {
        return defectType;
    }

    public void setDefectType(String defectType) {
        this.defectType = defectType;
    }

    public String getDefectLength() {
        return defectLength;
    }

    public void setDefectLength(String defectLength) {
        this.defectLength = defectLength;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
