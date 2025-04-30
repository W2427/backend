package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 放行单查询DTO
 */
public class ReleaseNoteItemPatchForRelnQtyDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "放行信息<key:relnItemId, value:relnQty>")
    Map<Long, BigDecimal> relnItemMap;

    public Map<Long, BigDecimal> getRelnItemMap() {
        return relnItemMap;
    }

    public void setRelnItemMap(Map<Long, BigDecimal> relnItemMap) {
        this.relnItemMap = relnItemMap;
    }
}
