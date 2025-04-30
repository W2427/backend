package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 更新配送清单DTO
 */
public class FMaterialTransfersPatchForRiceiveConfirmActivityDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "描述")
    private String fmtDesc;

    public String getFmtDesc() {
        return fmtDesc;
    }

    public void setFmtDesc(String fmtDesc) {
        this.fmtDesc = fmtDesc;
    }
}
