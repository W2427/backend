package com.ose.tasks.dto.material;

import java.math.BigDecimal;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 创建盘点OSD DTO
 */
public class FMaterialStocktakeOSDPatchDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "超出数量")
    private BigDecimal overQty;

    @Schema(description = "缺失数量")
    private BigDecimal shortageQty;

    public BigDecimal getOverQty() {
        return overQty;
    }

    public void setOverQty(BigDecimal overQty) {
        this.overQty = overQty;
    }

    public BigDecimal getShortageQty() {
        return shortageQty;
    }

    public void setShortageQty(BigDecimal shortageQty) {
        this.shortageQty = shortageQty;
    }

}
