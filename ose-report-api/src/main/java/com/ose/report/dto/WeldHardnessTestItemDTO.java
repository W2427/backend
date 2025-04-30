package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class WeldHardnessTestItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -8099685455613490502L;

    @Schema(description = "焊口")
    private String weldNo;

    @Schema(description = "母材")
    private String material;

    @Schema(description = "测试点1")
    private String testPoint1;

    @Schema(description = "测试点2")
    private String testPoint2;

    @Schema(description = "测试点3")
    private String testPoint3;

    @Schema(description = "测试点4")
    private String testPoint4;

    @Schema(description = "测试点5")
    private String testPoint5;

    @Schema(description = "测试点6")
    private String testPoint6;

    @Schema(description = "平均值")
    private String averageValue;

    @Schema(description = "标准值")
    private String standardValue;

    @Schema(description = "评估")
    private String evaluation;

    @Schema(description = "备注")
    private String remark;

    public String getWeldNo() {
        return weldNo;
    }

    public void setWeldNo(String weldNo) {
        this.weldNo = weldNo;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getTestPoint1() {
        return testPoint1;
    }

    public void setTestPoint1(String testPoint1) {
        this.testPoint1 = testPoint1;
    }

    public String getTestPoint2() {
        return testPoint2;
    }

    public void setTestPoint2(String testPoint2) {
        this.testPoint2 = testPoint2;
    }

    public String getTestPoint3() {
        return testPoint3;
    }

    public void setTestPoint3(String testPoint3) {
        this.testPoint3 = testPoint3;
    }

    public String getTestPoint4() {
        return testPoint4;
    }

    public void setTestPoint4(String testPoint4) {
        this.testPoint4 = testPoint4;
    }

    public String getTestPoint5() {
        return testPoint5;
    }

    public void setTestPoint5(String testPoint5) {
        this.testPoint5 = testPoint5;
    }

    public String getTestPoint6() {
        return testPoint6;
    }

    public void setTestPoint6(String testPoint6) {
        this.testPoint6 = testPoint6;
    }

    public String getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(String averageValue) {
        this.averageValue = averageValue;
    }

    public String getStandardValue() {
        return standardValue;
    }

    public void setStandardValue(String standardValue) {
        this.standardValue = standardValue;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
