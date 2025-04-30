package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public class PenetrationInspectionItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -1785990198931117259L;

    @Schema(description = "管线号")
    private String lineNo;

    @Schema(description = "焊口号")
    private String weldNo;

    @Schema(description = "焊工号")
    private String welderNo;

    @Schema(description = "工件规格")
    private String size;

    @Schema(description = "材料")
    private String material;

    @Schema(description = "DP")
    private String defectPos;

    @Schema(description = "DI")
    private String defectIdentifiy;

    @Schema(description = "DL")
    private String defectLength;

    @Schema(description = "意见")
    private String judge;

    @Schema(description = "备注")
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDefectPos() {
        return defectPos;
    }

    public void setDefectPos(String defectPos) {
        this.defectPos = defectPos;
    }

    public String getDefectIdentifiy() {
        return defectIdentifiy;
    }

    public void setDefectIdentifiy(String defectIdentifiy) {
        this.defectIdentifiy = defectIdentifiy;
    }

    public String getDefectLength() {
        return defectLength;
    }

    public void setDefectLength(String defectLength) {
        this.defectLength = defectLength;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
