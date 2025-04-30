package com.ose.tasks.dto.material;

import java.math.BigDecimal;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 更新盘点OSD DTO
 */
public class FMaterialStocktakeOSDPostDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "放行单ID")
    private Long relnId;

    @Schema(description = "放行单明细ID")
    private Long relnItemId;

    @Schema(description = "超出数量")
    private BigDecimal overQty;

    @Schema(description = "缺失数量")
    private BigDecimal shortageQty;

    public Long getRelnId() {
        return relnId;
    }

    public void setRelnId(Long relnId) {
        this.relnId = relnId;
    }

    public Long getRelnItemId() {
        return relnItemId;
    }

    public void setRelnItemId(Long relnItemId) {
        this.relnItemId = relnItemId;
    }

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
