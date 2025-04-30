package com.ose.tasks.dto.drawing;

import com.ose.dto.PageDTO;
import com.ose.tasks.vo.drawing.DrawingCoordinateType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 坐标实体子类型列表查询DTO
 */
public class DrawingCoordinateEntitySubTypeCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -7655395877912107210L;
    @Schema(description = "工序阶段id")
    Long entityTypeId;

    public Long getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(Long entityTypeId) {
        this.entityTypeId = entityTypeId;
    }


}
