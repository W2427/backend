package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class FinalInspectionItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 6409933809175151799L;

    @Schema(description = "版本号")
    private String revision;

    @Schema(description = "管件号")
    private String pieceNo;

    @Schema(description = "预制/现场")
    private String shopField;

    @Schema(description = "焊口号")
    private String weldNo;

    @Schema(description = "寸径")
    private String size;

    @Schema(description = "接头形式")
    private String jointType;

    @Schema(description = "焊工编号")
    private String welderNo;

    @Schema(description = "wps")
    private String wps;

    @Schema(description = "管/构件1材料形式")
    private String materialType1;

    @Schema(description = "管/构件1材质")
    private String type1;

    @Schema(description = "管/构件1炉批号")
    private String heatBatchNo1;

    @Schema(description = "管/构件2材料形式")
    private String materialType2;

    @Schema(description = "管/构件2材质")
    private String type2;

    @Schema(description = "管/构件2炉批号")
    private String heatBatchNo2;

    @Schema(description = "外观检验")
    private String vt;

    public String getPieceNo() {
        return pieceNo;
    }

    public void setPieceNo(String pieceNo) {
        this.pieceNo = pieceNo;
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

    public String getWelderNo() {
        return welderNo;
    }

    public void setWelderNo(String welderNo) {
        this.welderNo = welderNo;
    }

    public String getWps() {
        return wps;
    }

    public void setWps(String wps) {
        this.wps = wps;
    }

    public String getHeatBatchNo1() {
        return heatBatchNo1;
    }

    public void setHeatBatchNo1(String heatBatchNo1) {
        this.heatBatchNo1 = heatBatchNo1;
    }

    public String getHeatBatchNo2() {
        return heatBatchNo2;
    }

    public void setHeatBatchNo2(String heatBatchNo2) {
        this.heatBatchNo2 = heatBatchNo2;
    }

    public String getVt() {
        return vt;
    }

    public void setVt(String vt) {
        this.vt = vt;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getMaterialType1() {
        return materialType1;
    }

    public void setMaterialType1(String materialType1) {
        this.materialType1 = materialType1;
    }

    public String getMaterialType2() {
        return materialType2;
    }

    public void setMaterialType2(String materialType2) {
        this.materialType2 = materialType2;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getJointType() {
        return jointType;
    }

    public void setJointType(String jointType) {
        this.jointType = jointType;
    }
}
