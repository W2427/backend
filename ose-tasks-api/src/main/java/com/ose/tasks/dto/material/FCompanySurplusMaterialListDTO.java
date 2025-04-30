package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public class FCompanySurplusMaterialListDTO extends BaseDTO {
    private static final long serialVersionUID = -2101940526667579504L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "IDENT码")
    private String ident;

    @Schema(description = "施工单位")
    private String companyName;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "规格")
    private String spec;

    @Schema(description = "NPS")
    private String nps;

    @Schema(description = "炉批号ID")
    private Long heatNoId;

    @Schema(description = "炉批号")
    private String heatNoCode;

    @Schema(description = "计量单位")
    private String unitCode;

    @Schema(description = "入库数量")
    private BigDecimal recvQty;

    @Schema(description = "出库数量")
    private BigDecimal usedQty;

    @Schema(description = "锁定数量")
    private BigDecimal lockedQty;

    @Schema(description = "库存数量")
    private BigDecimal surplusQty;

    @Schema(description = "描述")
    private String shortDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public BigDecimal getRecvQty() {
        return recvQty;
    }

    public void setRecvQty(BigDecimal recvQty) {
        this.recvQty = recvQty;
    }

    public BigDecimal getUsedQty() {
        return usedQty;
    }

    public void setUsedQty(BigDecimal usedQty) {
        this.usedQty = usedQty;
    }

    public BigDecimal getLockedQty() {
        return lockedQty;
    }

    public void setLockedQty(BigDecimal lockedQty) {
        this.lockedQty = lockedQty;
    }

    public BigDecimal getSurplusQty() {
        return surplusQty;
    }

    public void setSurplusQty(BigDecimal surplusQty) {
        this.surplusQty = surplusQty;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getNps() {
        return nps;
    }

    public void setNps(String nps) {
        this.nps = nps;
    }

    public Long getHeatNoId() {
        return heatNoId;
    }

    public void setHeatNoId(Long heatNoId) {
        this.heatNoId = heatNoId;
    }

    public String getHeatNoCode() {
        return heatNoCode;
    }

    public void setHeatNoCode(String heatNoCode) {
        this.heatNoCode = heatNoCode;
    }
}
