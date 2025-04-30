package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PrefabricatePipingReleaseReportBeforeCoatingItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -6117469427746103705L;

    @Schema(description = "管线号")
    private String spoolNo;

    @Schema(description = "焊口数量")
    private Integer weldCount;

    @Schema(description = "版次")
    private String revision;

    @Schema(description = "焊口数")
    private String jointQuantity;

    @Schema(description = "焊后")
    private String afterWelding;

    @Schema(description = "油漆类型")
    private String paintCode;

    @Schema(description = "无损检测")
    private String ndt;

    @Schema(description = "压力")
    private String pressureTest;

    @Schema(description = "光谱")
    private String pmi;

    @Schema(description = "热处理")
    private String pwht;

    @Schema(description = "清洁")
    private String clean;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "规格")
    private String size;

    @Schema(description = "材料")
    private String material;

    @Schema(description = "尺寸")
    private String diesional;

    @Schema(description = "结果")
    private String result;

    public String getSpoolNo() {
        return spoolNo;
    }

    public void setSpoolNo(String spoolNo) {
        this.spoolNo = spoolNo;
    }

    public Integer getWeldCount() {
        return weldCount;
    }

    public void setWeldCount(Integer weldCount) {
        this.weldCount = weldCount;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getJointQuantity() {
        return jointQuantity;
    }

    public void setJointQuantity(String jointQuantity) {
        this.jointQuantity = jointQuantity;
    }

    public String getAfterWelding() {
        return afterWelding;
    }

    public void setAfterWelding(String afterWelding) {
        this.afterWelding = afterWelding;
    }

    public String getPaintCode() {
        return paintCode;
    }

    public void setPaintCode(String paintCode) {
        this.paintCode = paintCode;
    }

    public String getNdt() {
        return ndt;
    }

    public void setNdt(String ndt) {
        this.ndt = ndt;
    }

    public String getPressureTest() {
        return pressureTest;
    }

    public void setPressureTest(String pressureTest) {
        this.pressureTest = pressureTest;
    }

    public String getPmi() {
        return pmi;
    }

    public void setPmi(String pmi) {
        this.pmi = pmi;
    }

    public String getPwht() {
        return pwht;
    }

    public void setPwht(String pwht) {
        this.pwht = pwht;
    }

    public String getClean() {
        return clean;
    }

    public void setClean(String clean) {
        this.clean = clean;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDiesional() {
        return diesional;
    }

    public void setDiesional(String diesional) {
        this.diesional = diesional;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
