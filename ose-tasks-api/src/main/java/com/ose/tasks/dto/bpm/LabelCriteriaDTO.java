package com.ose.tasks.dto.bpm;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 标签列表查询DTO
 */
public class LabelCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = 9220358660995130303L;
    /**
     *
     */
    @Schema(description = "名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
