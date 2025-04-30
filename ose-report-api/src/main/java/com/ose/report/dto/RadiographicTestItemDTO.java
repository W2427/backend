package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class RadiographicTestItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 3254241995227691169L;

    @Schema(description = "构件号")
    private String partNo;

    @Schema(description = "焊工号")
    private String welderNo;

    @Schema(description = "焊接节点号")
    private String jointNo;

    @Schema(description = "焊接方法")
    private String process;

    @Schema(description = "片位号")
    private String filmNo;

    @Schema(description = "W.L")
    private String wl;

    @Schema(description = "材质")
    private String material;


    @Schema(description = "直径")
    private String dia;

    @Schema(description = "厚度")
    private String thk;

    @Schema(description = "渗透剂指数")
    private String iqi;

    @Schema(description = "黑度")
    private String density;

    @Schema(description = "缺陷类型")
    private String defectType;

    @Schema(description = "缺陷长度")
    private String defectLength;

    @Schema(description = "结果判定")
    private String judge;

    @Schema(description = "结果判定日期")
    private Date date;

    @Schema(description = "备注")
    private String remark;

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getWelderNo() {
        return welderNo;
    }

    public void setWelderNo(String welderNo) {
        this.welderNo = welderNo;
    }

    public String getJointNo() {
        return jointNo;
    }

    public void setJointNo(String jointNo) {
        this.jointNo = jointNo;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getFilmNo() {
        return filmNo;
    }

    public void setFilmNo(String filmNo) {
        this.filmNo = filmNo;
    }

    public String getWl() {
        return wl;
    }

    public void setWl(String wl) {
        this.wl = wl;
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

    public String getThk() {
        return thk;
    }

    public void setThk(String thk) {
        this.thk = thk;
    }

    public String getIqi() {
        return iqi;
    }

    public void setIqi(String iqi) {
        this.iqi = iqi;
    }

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
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

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
