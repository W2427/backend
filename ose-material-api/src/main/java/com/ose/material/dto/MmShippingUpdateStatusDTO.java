package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.vo.ShippingStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 发货单修改DTO
 */
public class MmShippingUpdateStatusDTO extends BaseDTO {

    private static final long serialVersionUID = -625753607121272858L;
    @Schema(description = "公司ID")
    private ShippingStatus shippingStatus;

    public ShippingStatus getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(ShippingStatus shippingStatus) {
        this.shippingStatus = shippingStatus;
    }
}
