package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class InspectionReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 3696757511947322903L;

    @Schema(description = "管件号")
    private String pieceNo;

    @Schema(description = "区域号")
    private String areaNo;

    @Schema(description = "预制/现场")
    private String shopField;

    @Schema(description = "焊口号")
    private String weldNo;

    @Schema(description = "寸径")
    private String size;

    @Schema(description = "管/构件1名称")
    private String componentType1;

    @Schema(description = "管/构件1炉批号")
    private String heatBatchNo1;

    @Schema(description = "管/构件2名称")
    private String componentType2;

    @Schema(description = "管/构件2炉批号")
    private String heatBatchNo2;

    @Schema(description = "尺寸检验")
    private String demensionCheck;

    @Schema(description = "结果")
    private String result;

    @Schema(description = "外观检验")
    private String vt;

    public String getPieceNo() {
        return pieceNo;
    }

    public void setPieceNo(String pieceNo) {
        this.pieceNo = pieceNo;
    }

    public String getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(String areaNo) {
        this.areaNo = areaNo;
    }

    public String getShopField() {
        return shopField;
    }

    public void setShopField(String shopField) {
        this.shopField = shopField;
    }

    public String getWeldNo() {
        return weldNo;
    }

    public void setWeldNo(String weldNo) {
        this.weldNo = weldNo;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getComponentType1() {
        return componentType1;
    }

    public void setComponentType1(String componentType1) {
        this.componentType1 = componentType1;
    }

    public String getHeatBatchNo1() {
        return heatBatchNo1;
    }

    public void setHeatBatchNo1(String heatBatchNo1) {
        this.heatBatchNo1 = heatBatchNo1;
    }

    public String getComponentType2() {
        return componentType2;
    }

    public void setComponentType2(String componentType2) {
        this.componentType2 = componentType2;
    }

    public String getHeatBatchNo2() {
        return heatBatchNo2;
    }

    public void setHeatBatchNo2(String heatBatchNo2) {
        this.heatBatchNo2 = heatBatchNo2;
    }

    public String getDemensionCheck() {
        return demensionCheck;
    }

    public void setDemensionCheck(String demensionCheck) {
        this.demensionCheck = demensionCheck;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getVt() {
        return vt;
    }

    public void setVt(String vt) {
        this.vt = vt;
    }
}
