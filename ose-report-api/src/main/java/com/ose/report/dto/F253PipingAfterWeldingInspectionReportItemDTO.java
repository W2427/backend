package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class F253PipingAfterWeldingInspectionReportItemDTO extends BaseReportListItemDTO{

    private static final long serialVersionUID = -387092728002976542L;

    @Schema(name = "编号")
    private String sn;

    @Schema(name = "图纸号")
    private String drawingNo;

    @Schema(name = "版本")
    private String rev;

    @Schema(name = "焊口号")
    private String jointNo;

    @Schema(name = "关联类型")
    private String jointType;

    @Schema(name = "序号")
    private String sf;

    @Schema(name = "焊接工序")
    private String weldingProcess;

    @Schema(name = "wps号")
    private String wps;

    @Schema(name = "焊接材料")
    private String weldingConsumable;

    @Schema(name = "批号")
    private String batchNo;

    @Schema(name = "壁厚值")
    private String ns;

    @Schema(name = "焊接材料")
    private String welderID;

    @Schema(name = "结果")
    private String result;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getJointNo() {
        return jointNo;
    }

    public void setJointNo(String jointNo) {
        this.jointNo = jointNo;
    }

    public String getJointType() {
        return jointType;
    }

    public void setJointType(String jointType) {
        this.jointType = jointType;
    }

    public String getSf() {
        return sf;
    }

    public void setSf(String sf) {
        this.sf = sf;
    }

    public String getWeldingProcess() {
        return weldingProcess;
    }

    public void setWeldingProcess(String weldingProcess) {
        this.weldingProcess = weldingProcess;
    }

    public String getWps() {
        return wps;
    }

    public void setWps(String wps) {
        this.wps = wps;
    }

    public String getWeldingConsumable() {
        return weldingConsumable;
    }

    public void setWeldingConsumable(String weldingConsumable) {
        this.weldingConsumable = weldingConsumable;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getNs() {
        return ns;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getWelderID() {
        return welderID;
    }

    public void setWelderID(String welderID) {
        this.welderID = welderID;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
