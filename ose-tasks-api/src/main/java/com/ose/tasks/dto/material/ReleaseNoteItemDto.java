package com.ose.tasks.dto.material;

import com.ose.dto.PageDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 放行单查询DTO
 */
public class ReleaseNoteItemDto extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "放行单ID")
    private Long relnId;

    @Schema(description = "放行单号")
    private String relnNumber;

    public Long getRelnId() {
        return relnId;
    }

    public void setRelnId(Long relnId) {
        this.relnId = relnId;
    }

    public String getRelnNumber() {
        return relnNumber;
    }

    public void setRelnNumber(String relnNumber) {
        this.relnNumber = relnNumber;
    }

}
