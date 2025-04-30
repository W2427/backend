package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 数据传输对象
 */
public class InspectionIssueCriteriaDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "流程实例id(多选时用,分隔)")
    private String actInstIds;

    public String getActInstIds() {
        return actInstIds;
    }

    public void setActInstIds(String actInstIds) {
        this.actInstIds = actInstIds;
    }

}
