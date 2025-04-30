package com.ose.test.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 出库清单详情DTO
 */
public class FMaterialIssueReceiptDetailDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "默认值：N")
    private String esiStatus;

    @Schema(description = "库存ID")
    private String iviId;

    @Schema(description = "发料单ID")
    private String mirId;

    @Schema(description = "料表明细ID")
    private String lpId;

    @Schema(description = "预测预留明细ID，直接出库为空")
    private String ivprId;

    @Schema(description = "发料数量")
    private BigDecimal issueQty;

    @Schema(description = "发料日期")
    private Date issueDate;

    @Schema(description = "材料编码")
    private String ident;

    @Schema(description = "仓库ID")
    private String whId;

    @Schema(description = "库位ID")
    private String locId;

    @Schema(description = "材料状态")
    private String smstId;

    @Schema(description = "单位")
    private String unitId;

    @Schema(description = "位号")
    private String tagNumber;

    @Schema(description = "炉批号ID")
    private String heatId;

    @Schema(description = "默认值：NULL")
    private String plateId;

    @Schema(description = "代材描述，一般为空")
    private String identDeviation;

    @Schema(description = "默认值：NULL")
    private String sasId;

    @Schema(description = "默认值：N")
    private String siteStatInd;

    @Schema(description = "出库单号")
    private String mirNumber;

    public String getEsiStatus() {
        return esiStatus;
    }

    public void setEsiStatus(String esiStatus) {
        this.esiStatus = esiStatus;
    }

    public String getIviId() {
        return iviId;
    }

    public void setIviId(String iviId) {
        this.iviId = iviId;
    }

    public String getMirId() {
        return mirId;
    }

    public void setMirId(String mirId) {
        this.mirId = mirId;
    }

    public String getLpId() {
        return lpId;
    }

    public void setLpId(String lpId) {
        this.lpId = lpId;
    }

    public String getIvprId() {
        return ivprId;
    }

    public void setIvprId(String ivprId) {
        this.ivprId = ivprId;
    }

    public BigDecimal getIssueQty() {
        return issueQty;
    }

    public void setIssueQty(BigDecimal issueQty) {
        this.issueQty = issueQty;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getWhId() {
        return whId;
    }

    public void setWhId(String whId) {
        this.whId = whId;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getSmstId() {
        return smstId;
    }

    public void setSmstId(String smstId) {
        this.smstId = smstId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getHeatId() {
        return heatId;
    }

    public void setHeatId(String heatId) {
        this.heatId = heatId;
    }

    public String getPlateId() {
        return plateId;
    }

    public void setPlateId(String plateId) {
        this.plateId = plateId;
    }

    public String getIdentDeviation() {
        return identDeviation;
    }

    public void setIdentDeviation(String identDeviation) {
        this.identDeviation = identDeviation;
    }

    public String getSasId() {
        return sasId;
    }

    public void setSasId(String sasId) {
        this.sasId = sasId;
    }

    public String getSiteStatInd() {
        return siteStatInd;
    }

    public void setSiteStatInd(String siteStatInd) {
        this.siteStatInd = siteStatInd;
    }

    public String getMirNumber() {
        return mirNumber;
    }

    public void setMirNumber(String mirNumber) {
        this.mirNumber = mirNumber;
    }
}
