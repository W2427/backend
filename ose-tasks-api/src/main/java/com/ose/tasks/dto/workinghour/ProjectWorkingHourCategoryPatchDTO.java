package com.ose.tasks.dto.workinghour;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 工时DTO
 */
public class ProjectWorkingHourCategoryPatchDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "材料盘点单号")
    private Long fmstId;

    public Long getFmstId() {
        return fmstId;
    }

    public void setFmstId(Long fmstId) {
        this.fmstId = fmstId;
    }

}
