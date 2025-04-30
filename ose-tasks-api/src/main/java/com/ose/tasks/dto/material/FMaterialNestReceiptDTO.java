package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 套料单DTO
 */
public class FMaterialNestReceiptDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "材料准备单Id")
    private Long fmpId;

    @Schema(description = "描述")
    private String fmnrDesc;

    public Long getFmpId() {
        return fmpId;
    }

    public void setFmpId(Long fmpId) {
        this.fmpId = fmpId;
    }

    public String getFmnrDesc() {
        return fmnrDesc;
    }

    public void setFmnrDesc(String fmnrDesc) {
        this.fmnrDesc = fmnrDesc;
    }
}
