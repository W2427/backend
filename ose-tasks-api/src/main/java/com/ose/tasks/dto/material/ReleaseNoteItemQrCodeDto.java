package com.ose.tasks.dto.material;

import com.ose.dto.PageDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 放行单查询DTO
 */
public class ReleaseNoteItemQrCodeDto extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "放行单详情ID")
    private Long relnItemDetailId;

    public Long getRelnItemDetailId() {
        return relnItemDetailId;
    }

    public void setRelnItemDetailId(Long relnItemDetailId) {
        this.relnItemDetailId = relnItemDetailId;
    }

}
