package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * 余料dto
 */
public class FCompanySurplusMaterialPatchToRecieveDTO extends BaseDTO {
    private static final long serialVersionUID = -3548931677298658302L;

    @Schema(description = "待确认的余料明细ID")
    private Long surplusMaterialItemId;

    @Schema(description = "确认后的材料长度")
    private BigDecimal qty;

    public Long getSurplusMaterialItemId() {
        return surplusMaterialItemId;
    }

    public void setSurplusMaterialItemId(Long surplusMaterialItemId) {
        this.surplusMaterialItemId = surplusMaterialItemId;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}
