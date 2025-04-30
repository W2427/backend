package com.ose.tasks.dto.material;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 库存查询--检索dto
 */
public class FItemQtyInfoCriteriaDTO extends PageDTO {
    private static final long serialVersionUID = -6373273605179155494L;

    @Schema(description = "ident码")
    private String ident;

    @Schema(description = "材料编号")
    private String tagNumber;

    @Schema(description = "材料规格")
    private String spec;

    @Schema(description = "炉批号")
    private String heatNo;

    @Schema(description = "NPS")
    private Double nps;

    public Double getNps() {
        return nps;
    }

    public void setNps(Double nps) {
        this.nps = nps;
    }

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

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
    }
}
