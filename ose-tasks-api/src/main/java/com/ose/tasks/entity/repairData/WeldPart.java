package com.ose.tasks.entity.repairData;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "weld_part",
    indexes = {
        @Index(columnList = "entityId")
    })
public class WeldPart extends BaseEntity {

    private static final long serialVersionUID = 2149967119568273001L;
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

    @Schema(description = "joint type")
    private String jointType;

    @Schema(description = "wps")
    private String wps;

    @Schema(description = "size")
    private String size;

    @Schema(description = "thickness")
    private String thickness;

    @Schema(description = "welder id")
    private String welderId;

    @Schema(description = "weld date")
    private String weldDate;

    @Schema(description = "report_no")
    private String reportNo;

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

    public String getJointType() {
        return jointType;
    }

    public void setJointType(String jointType) {
        this.jointType = jointType;
    }

    public String getWelderId() {
        return welderId;
    }

    public void setWelderId(String welderId) {
        this.welderId = welderId;
    }

    public String getWeldDate() {
        return weldDate;
    }

    public void setWeldDate(String weldDate) {
        this.weldDate = weldDate;
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
