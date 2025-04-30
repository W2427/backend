package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 发货单详情修改发货量DTO
 */
public class MmShippingDetailUpdateQtyDTO extends BaseDTO {

    private static final long serialVersionUID = -2923365155544130186L;
    @Schema(description = "修改量")
    public Integer qty;

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
