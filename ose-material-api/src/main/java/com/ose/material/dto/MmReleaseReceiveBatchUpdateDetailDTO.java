package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 入库材料二维码查询DTO
 */
public class MmReleaseReceiveBatchUpdateDetailDTO extends BaseDTO {

    private static final long serialVersionUID = 4777614565583583025L;

    @Schema(description = "入库详情ID")
    public Long materialReceiveNoteDetailId;

    @Schema(description = "数量")
    public Integer qty;

    public Long getMaterialReceiveNoteDetailId() {
        return materialReceiveNoteDetailId;
    }

    public void setMaterialReceiveNoteDetailId(Long materialReceiveNoteDetailId) {
        this.materialReceiveNoteDetailId = materialReceiveNoteDetailId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
