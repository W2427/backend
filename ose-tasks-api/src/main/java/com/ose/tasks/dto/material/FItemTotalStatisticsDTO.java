package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

/**
 * 更新放行单DTO
 */
public class FItemTotalStatisticsDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "ident码")
    private String ident;

    @Schema(description = "规格")
    private String spec;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS 单位")
    private LengthUnit npsUnit;

    @Schema(description = "NPS")
    private Double nps;

    @Schema(description = "单位")
    private String unitCode;

    @Schema(description = "描述")
    private String shortDesc;

    @Schema(description = "库存数量")
    private BigDecimal totalQty = BigDecimal.ZERO;

    @Schema(description = "入库数量")
    private BigDecimal recvQty = BigDecimal.ZERO;

    @Schema(description = "出库数量")
    private BigDecimal issueQty = BigDecimal.ZERO;

    @Schema(description = "单位量,数量的DTO列表")
    private List<FItemStatisticsDTO> fItemStatisticsDTOList;

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
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

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public BigDecimal getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(BigDecimal totalQty) {
        this.totalQty = totalQty;
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

    public List<FItemStatisticsDTO> getfItemStatisticsDTOList() {
        return fItemStatisticsDTOList;
    }

    public void setfItemStatisticsDTOList(List<FItemStatisticsDTO> fItemStatisticsDTOList) {
        this.fItemStatisticsDTOList = fItemStatisticsDTOList;
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
}
