package com.ose.tasks.entity.repairData;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "pwht",
    indexes = {
        @Index(columnList = "entityId")
    })
public class PwhtPart extends BaseEntity {

    private static final long serialVersionUID = -195176761859653134L;
    @Schema(description = "chart No")
    private String chartNo;

    @Schema(description = "iso")
    private String iso;

    @Schema(description = "spool no")
    private String spoolNo;

    @Schema(description = "joint no")
    private String jointNo;

    @Schema(description = "repair")
    private String repair;

    @Schema(description = "material")
    private String material;

    @Schema(description = "dn")
    private String dn;

    @Schema(description = "thickness")
    private String thickness;

    @Schema(description = "report No")
    private String reportNo;

    @Schema(description = "date")
    private String date;

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

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getSpoolNo() {
        return spoolNo;
    }

    public void setSpoolNo(String spoolNo) {
        this.spoolNo = spoolNo;
    }

    public String getChartNo() {
        return chartNo;
    }

    public void setChartNo(String chartNo) {
        this.chartNo = chartNo;
    }

    public String getJointNo() {
        return jointNo;
    }

    public void setJointNo(String jointNo) {
        this.jointNo = jointNo;
    }

    public String getRepair() {
        return repair;
    }

    public void setRepair(String repair) {
        this.repair = repair;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
