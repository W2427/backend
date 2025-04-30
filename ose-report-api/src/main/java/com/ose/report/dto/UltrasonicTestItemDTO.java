package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class UltrasonicTestItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 3604620887883140066L;

    @Schema(description = "焊缝节点号")
    private String weldNo;

    @Schema(description = "焊工号")
    private String welderNo;

    @Schema(description = "构厚度件号")
    private String thickness;

    @Schema(description = "焊缝长度")
    private String wl;

    @Schema(description = "探头角度")
    private String probeAngle;

    @Schema(description = "缺陷号")
    private String defectNo;

    @Schema(description = "参考点")
    private String referencePoint;

    private String x;

    private String y;

    private String w;

    private String d;

    private String l;

    @Schema(description = "范围")
    private String range;

    @Schema(description = "检测日期")
    private Date date;

    @Schema(description = "意见")
    private String judge;

    @Schema(description = "返修报告号")
    private String repairReportNo;

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

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    public String getWl() {
        return wl;
    }

    public void setWl(String wl) {
        this.wl = wl;
    }

    public String getProbeAngle() {
        return probeAngle;
    }

    public void setProbeAngle(String probeAngle) {
        this.probeAngle = probeAngle;
    }

    public String getDefectNo() {
        return defectNo;
    }

    public void setDefectNo(String defectNo) {
        this.defectNo = defectNo;
    }

    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getRepairReportNo() {
        return repairReportNo;
    }

    public void setRepairReportNo(String repairReportNo) {
        this.repairReportNo = repairReportNo;
    }

}
