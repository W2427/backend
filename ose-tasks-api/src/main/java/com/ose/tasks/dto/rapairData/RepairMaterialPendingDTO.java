package com.ose.tasks.dto.rapairData;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 修复外检流程卡住的数据。
 */

public class RepairMaterialPendingDTO extends BaseDTO {

    private static final long serialVersionUID = -2433112299628536380L;

    @Schema(description = "材料准备单号")
    private String mpCode;

    public String getMpCode() {
        return mpCode;
    }

    public void setMpCode(String mpCode) {
        this.mpCode = mpCode;
    }
}
