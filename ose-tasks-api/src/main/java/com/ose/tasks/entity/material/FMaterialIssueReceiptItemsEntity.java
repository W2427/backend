package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 入库报告实体类。
 */
@Entity
@Table(name = "mat_f_material_issue_item")
public class FMaterialIssueReceiptItemsEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "出库清单ID")
    private Long fmirId;

    @Schema(description = "默认值：N")
    private String esiStatus;

    @Schema(description = "库存ID")
    private String iviId;

    @Schema(description = "料表明细ID")
    private String lpId;

    @Schema(description = "预测预留明细ID")
    private String ivprId;

    @Schema(description = "发料数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal resvQty;

    @Schema(description = "发料数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal issueQty;

    @Schema(description = "发料日期")
    private Date issueDate;

    @Schema(description = "IDENT码")
    private String ident;

    @Schema(description = "仓库ID")
    private String whId;

    @Schema(description = "仓库")
    private String whCode;

    @Schema(description = "库位ID")
    private String locId;

    @Schema(description = "库位")
    private String locCode;

    @Schema(description = "材料状态ID")
    private String smstId;

    @Schema(description = "材料状态")
    private String smstCode;

    @Schema(description = "单位ID")
    private String unitId;

    @Schema(description = "单位")
    private String unitCode;

    @Schema(description = "位号")
    private String tagNumber;

    @Schema(description = "炉批号ID")
    private String heatId;

    @Schema(description = "炉批号")
    private String heatNumber;

    @Schema(description = "默认值：NULL")
    private String plateId;

    @Schema(description = "代材描述，一般为空")
    private String identDeviation;

    @Schema(description = "默认值：NULL")
    private String sasId;

    @Schema(description = "默认值：N")
    private String siteStatInd;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getFmirId() {
        return fmirId;
    }

    public void setFmirId(Long fmirId) {
        this.fmirId = fmirId;
    }

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

    public BigDecimal getResvQty() {
        return resvQty;
    }

    public void setResvQty(BigDecimal resvQty) {
        this.resvQty = resvQty;
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

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getSmstId() {
        return smstId;
    }

    public void setSmstId(String smstId) {
        this.smstId = smstId;
    }

    public String getSmstCode() {
        return smstCode;
    }

    public void setSmstCode(String smstCode) {
        this.smstCode = smstCode;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
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

    public String getHeatNumber() {
        return heatNumber;
    }

    public void setHeatNumber(String heatNumber) {
        this.heatNumber = heatNumber;
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
}
