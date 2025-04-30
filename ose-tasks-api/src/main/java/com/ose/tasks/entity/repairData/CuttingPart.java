package com.ose.tasks.entity.repairData;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "cutting_part",
indexes = {
    @Index(columnList = "entityId"),
    @Index(columnList = "heatNo")
})
public class CuttingPart extends BaseEntity {


    private static final long serialVersionUID = -6842548565705859049L;

    @Schema(description = "no")
    private String no;

    @Schema(description = "rev")
    private String rev;

    @Schema(description = "joint no")
    private String jointNo;

    @Schema(description = "ident")
    private String ident;

    @Schema(description = "description")
    private String description;

    @Schema(description = "dn")
    private String dn;

    @Schema(description = "thickness")
    private String thickness;

    @Schema(description = "qty")
    private Integer qty;

    @Schema(description = "qr_code")
    private String qrCode;

    @Schema(description = "heat no")
    private String heatNo;

    @Schema(description = "batch No")
    private String batchNo;

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

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
