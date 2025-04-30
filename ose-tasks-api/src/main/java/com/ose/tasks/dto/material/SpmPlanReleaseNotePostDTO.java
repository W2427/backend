package com.ose.tasks.dto.material;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SPM 计划放行单 查询DTO
 */
public class SpmPlanReleaseNotePostDTO extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "SPM 计划放行单号")
    private String relnNumber;

    public String getRelnNumber() {
        return relnNumber;
    }

    public void setRelnNumber(String relnNumber) {
        this.relnNumber = relnNumber;
    }

}
