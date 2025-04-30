package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * FItem库存信息
 */
public class FItemQtyInformationDTO extends BaseDTO {
    private static final long serialVersionUID = 5294628023226680295L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "IDENT码")
    private String ident;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "规格")
    private String spec;

    @Schema(description = "NPS 表示值")
    private String npsText;

//    @Schema(description = "NPS 单位")
//    private LengthUnit npsUnit;

//    @Schema(description = "NPS")
//    private Double nps;

    @Schema(description = "计量单位")
    private String unitCode;

    @Schema(description = "入库数量")
    private BigDecimal recvQty;

    @Schema(description = "出库数量")
    private BigDecimal issueQty;

    @Schema(description = "库存数量")
    private BigDecimal qty;

    @Schema(description = "描述")
    private String shortDesc;

    @Schema(description = "texture")
    private String texture;

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
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

    public BigDecimal getIssueQty() {
        return issueQty;
    }

    public void setIssueQty(BigDecimal issueQty) {
        this.issueQty = issueQty;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNpsText() {
        return npsText;
    }

    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }
//    public LengthUnit getNpsUnit() {
//        return npsUnit;
//    }
//
//    public void setNpsUnit(LengthUnit npsUnit) {
//        this.npsUnit = npsUnit;
//    }
//
//    public Double getNps() {
//        return nps;
//    }
//
//    public void setNps(Double nps) {
//        this.nps = nps;
//    }
}
