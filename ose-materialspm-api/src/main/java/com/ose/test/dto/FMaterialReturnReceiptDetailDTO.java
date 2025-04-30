package com.ose.test.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * 退库清单详情DTO
 */
public class FMaterialReturnReceiptDetailDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "默认值：N")
    private String esiStatus;

    @Schema(description = "退库单ID")
    private String mirtiId;

    @Schema(description = "仓库ID")
    private String whId;

    @Schema(description = "库位ID")
    private String locId;

    @Schema(description = "材料状态")
    private String smstId;

    @Schema(description = "退库数量")
    private BigDecimal returnQty;

    @Schema(description = "单位")
    private String unitId;

    @Schema(description = "退库单号")
    private String rtiNumber;

    @Schema(description = "SPM PROJECT ID")
    private String projId;

    @Schema(description = "默认值：NULL")
    private String iisId;

    @Schema(description = "预测预留明细ID，直接出库为空")
    private String ivprId;

    @Schema(description = "出库单号")
    private String mirNumber;

    public String getEsiStatus() {
        return esiStatus;
    }

    public void setEsiStatus(String esiStatus) {
        this.esiStatus = esiStatus;
    }

    public String getMirtiId() {
        return mirtiId;
    }

    public void setMirtiId(String mirtiId) {
        this.mirtiId = mirtiId;
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

    public BigDecimal getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(BigDecimal returnQty) {
        this.returnQty = returnQty;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getRtiNumber() {
        return rtiNumber;
    }

    public void setRtiNumber(String rtiNumber) {
        this.rtiNumber = rtiNumber;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getIisId() {
        return iisId;
    }

    public void setIisId(String iisId) {
        this.iisId = iisId;
    }

    public String getIvprId() {
        return ivprId;
    }

    public void setIvprId(String ivprId) {
        this.ivprId = ivprId;
    }

    public String getMirNumber() {
        return mirNumber;
    }

    public void setMirNumber(String mirNumber) {
        this.mirNumber = mirNumber;
    }
}
