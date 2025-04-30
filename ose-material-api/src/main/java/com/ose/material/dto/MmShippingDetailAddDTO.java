package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 发货单详情添加DTO
 */
public class MmShippingDetailAddDTO extends BaseDTO {

    private static final long serialVersionUID = -8521880382218919937L;
    @Schema(description = "请购详情")
    public List<MmShippingDetailsDTO> mmShippingDetailsDTOS;

    public List<MmShippingDetailsDTO> getMmShippingDetailsDTOS() {
        return mmShippingDetailsDTOS;
    }

    public void setMmShippingDetailsDTOS(List<MmShippingDetailsDTO> mmShippingDetailsDTOS) {
        this.mmShippingDetailsDTOS = mmShippingDetailsDTOS;
    }
}
