package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 是否完成盘库查询DTO
 */
public class MmReleaseReceiveInventoryStatusDTO extends BaseDTO {

    private static final long serialVersionUID = -7888793736241611450L;

    @Schema(description = "是否完成盘库")
    private Boolean inventory;

    public Boolean getInventory() {
        return inventory;
    }

    public void setInventory(Boolean inventory) {
        this.inventory = inventory;
    }
}
