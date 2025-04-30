package com.ose.tasks.entity.repairData;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "fitup_part",
    indexes = {
        @Index(columnList = "entityId"),
        @Index(columnList = "reportNo")
    })
public class FitupPart extends BaseEntity {


    private static final long serialVersionUID = -6842548565705859049L;

    @Schema(description = "report_no")
    private String reportNo;

    @Schema(description = "dwg")
    private String dwg;

    @Schema(description = "rev")
    private String rev;

    @Schema(description = "spool no")
    private String spoolNo;

    @Schema(description = "joint no")
    private String jointNo;

    @Schema(description = "repair")
    private String repair;

    @Schema(description = "wps")
    private String wps;

    @Schema(description = "size")
    private String size;

    @Schema(description = "thickness")
    private String thickness;

    @Schema(description = "group1")
    private String group1;

    @Schema(description = "material1")
    private String material1;

    @Schema(description = "heat no1")
    private String heatNo1;

    @Schema(description = "batch No1")
    private String batchNo1;

    @Schema(description = "size2")
    private String size2;

    @Schema(description = "thickness2")
    private String thickness2;

    @Schema(description = "group2")
    private String group2;

    @Schema(description = "material2")
    private String material2;

    @Schema(description = "heat no2")
    private String heatNo2;

    @Schema(description = "batch No2")
    private String batchNo2;

    @Schema(description = "date")
    private String date;

    @Schema(description = "result")
    private String result;

    @Schema(description = "no")
    private String no;

    @Schema(description = "entity Id")
    private Long entityId;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
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

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getDwg() {
        return dwg;
    }

    public void setDwg(String dwg) {
        this.dwg = dwg;
    }

    public String getSpoolNo() {
        return spoolNo;
    }

    public void setSpoolNo(String spoolNo) {
        this.spoolNo = spoolNo;
    }

    public String getRepair() {
        return repair;
    }

    public void setRepair(String repair) {
        this.repair = repair;
    }

    public String getWps() {
        return wps;
    }

    public void setWps(String wps) {
        this.wps = wps;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    public String getGroup1() {
        return group1;
    }

    public void setGroup1(String group1) {
        this.group1 = group1;
    }

    public String getMaterial1() {
        return material1;
    }

    public void setMaterial1(String material1) {
        this.material1 = material1;
    }

    public String getHeatNo1() {
        return heatNo1;
    }

    public void setHeatNo1(String heatNo1) {
        this.heatNo1 = heatNo1;
    }

    public String getBatchNo1() {
        return batchNo1;
    }

    public void setBatchNo1(String batchNo1) {
        this.batchNo1 = batchNo1;
    }

    public String getSize2() {
        return size2;
    }

    public void setSize2(String size2) {
        this.size2 = size2;
    }

    public String getThickness2() {
        return thickness2;
    }

    public void setThickness2(String thickness2) {
        this.thickness2 = thickness2;
    }

    public String getGroup2() {
        return group2;
    }

    public void setGroup2(String group2) {
        this.group2 = group2;
    }

    public String getMaterial2() {
        return material2;
    }

    public void setMaterial2(String material2) {
        this.material2 = material2;
    }

    public String getHeatNo2() {
        return heatNo2;
    }

    public void setHeatNo2(String heatNo2) {
        this.heatNo2 = heatNo2;
    }

    public String getBatchNo2() {
        return batchNo2;
    }

    public void setBatchNo2(String batchNo2) {
        this.batchNo2 = batchNo2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
