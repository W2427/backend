package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public class MagneticParticleInspectionItmeDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -7812605928737406899L;

    @Schema(description = "焊口号")
    private String weldNo;

    @Schema(description = "焊工号")
    private String welderNo;

    @Schema(description = "工件规格")
    private String size;

    @Schema(description = "W.L")
    private String weldLength;

    @Schema(description = "D.P")
    private String defectPos;

    @Schema(description = "D.I")
    private String defectIdentifiy;

    @Schema(description = "D.L")
    private String defectLength;

    @Schema(description = "意见")
    private String judge;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "检测日期")
    private Date date;

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

    public String getWeldLength() {
        return weldLength;
    }

    public void setWeldLength(String weldLength) {
        this.weldLength = weldLength;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
