package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import com.ose.vo.unit.LengthUnit;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;

/**
 * 二维码。
 */
public class QRCodeSearchResultEntityCuttingDTO extends BaseDTO {

    private static final long serialVersionUID = 3306151730639645725L;

    private String displayName = null;

    @Schema(description = "管段实体编号")
    private String pipePieceEntityNo;

    @Schema(description = "材料准备单号")
    private String matPrepareCode;

    @Schema(description = "材料出库单号")
    private String matIssueCode;

    @Schema(description = "材质描述")
    private String cuttingMaterialCode;

    @Schema(description = "nps")
    private String cuttingNps;

    @Schema(description = "坡口#1代码")
    private String bevelCode1;

    @Schema(description = "坡口#2代码")
    private String bevelCode2;

    @Schema(description = "弯管信息")
    private String bendInfo;

    @Schema(description = "材质代码")
    private String materialCode;

    @Schema(description = "材质描述")
    private String material;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS 单位")
    private LengthUnit npsUnit;

    @Schema(description = "NPS")
    private Double nps;

    @Schema(description = "长度表示值")
    private String lengthText;

    @Schema(description = "长度单位")
    private LengthUnit lengthUnit;

    @Schema(description = "长度")
    private Double length;

    @Schema(description = "所属管线实体编号")
    @Column
    private String isoNo;

    @Schema(description = "所属单管实体编号")
    private String spoolNo;

    @Schema(description = "炉批号")
    private String heatNo;

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
    }



    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMatPrepareCode() {
        return matPrepareCode;
    }

    public void setMatPrepareCode(String matPrepareCode) {
        this.matPrepareCode = matPrepareCode;
    }

    public String getMatIssueCode() {
        return matIssueCode;
    }

    public void setMatIssueCode(String matIssueCode) {
        this.matIssueCode = matIssueCode;
    }

    public String getBevelCode1() {
        return bevelCode1;
    }

    public void setBevelCode1(String bevelCode1) {
        this.bevelCode1 = bevelCode1;
    }

    public String getBevelCode2() {
        return bevelCode2;
    }

    public void setBevelCode2(String bevelCode2) {
        this.bevelCode2 = bevelCode2;
    }

    public String getBendInfo() {
        return bendInfo;
    }

    public void setBendInfo(String bendInfo) {
        this.bendInfo = bendInfo;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getNpsText() {
        return npsText;
    }

    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    public LengthUnit getNpsUnit() {
        return npsUnit;
    }

    public void setNpsUnit(LengthUnit npsUnit) {
        this.npsUnit = npsUnit;
    }

    public Double getNps() {
        return nps;
    }

    public void setNps(Double nps) {
        this.nps = nps;
    }

    public String getLengthText() {
        return lengthText;
    }

    public void setLengthText(String lengthText) {
        this.lengthText = lengthText;
    }

    public LengthUnit getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(LengthUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getIsoNo() {
        return isoNo;
    }

    public void setIsoNo(String isoNo) {
        this.isoNo = isoNo;
    }

    public String getSpoolNo() {
        return spoolNo;
    }

    public void setSpoolNo(String spoolNo) {
        this.spoolNo = spoolNo;
    }

    public String getCuttingMaterialCode() {
        return cuttingMaterialCode;
    }

    public void setCuttingMaterialCode(String cuttingMaterialCode) {
        this.cuttingMaterialCode = cuttingMaterialCode;
    }

    public String getCuttingNps() {
        return cuttingNps;
    }

    public void setCuttingNps(String cuttingNps) {
        this.cuttingNps = cuttingNps;
    }

    public String getPipePieceEntityNo() {
        return pipePieceEntityNo;
    }

    public void setPipePieceEntityNo(String pipePieceEntityNo) {
        this.pipePieceEntityNo = pipePieceEntityNo;
    }

}
