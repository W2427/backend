package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 创建配送清单DTO
 */
public class FMaterialTransferItemPatchDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "配送材料信息<key:fmtItemId, value:planTransferQty>")
    Map<Long, BigDecimal> transferMap;

    public Map<Long, BigDecimal> getTransferMap() {
        return transferMap;
    }

    public void setTransferMap(Map<Long, BigDecimal> transferMap) {
        this.transferMap = transferMap;
    }
}
