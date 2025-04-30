package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.material.FItemDetailEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

/**
 * 更新放行单DTO
 */
public class FItemStatisticsDTO extends BaseDTO {

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

    @Schema(description = "单位量")
    private BigDecimal qty;

    @Schema(description = "总件数")
    private int qtyCnt;

    private List<FItemDetailEntity> fItemDetailEntityList;

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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public int getQtyCnt() {
        return qtyCnt;
    }

    public void setQtyCnt(int qtyCnt) {
        this.qtyCnt = qtyCnt;
    }

    public List<FItemDetailEntity> getfItemDetailEntityList() {
        return fItemDetailEntityList;
    }

    public void setfItemDetailEntityList(List<FItemDetailEntity> fItemDetailEntityList) {
        this.fItemDetailEntityList = fItemDetailEntityList;
    }
}
