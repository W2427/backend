package com.ose.tasks.dto.wbs;

import com.ose.dto.PageDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class SpoolSplitSearchDTO extends PageDTO {

    private static final long serialVersionUID = 2331195486323614686L;

    @Schema(description = "要变更焊口id")
    private Long weldId;

    public Long getWeldId() {
        return weldId;
    }

    public void setWeldId(Long weldId) {
        this.weldId = weldId;
    }
}
