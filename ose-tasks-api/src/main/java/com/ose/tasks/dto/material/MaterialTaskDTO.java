package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 材料任务创建任务DTO
 */
public class MaterialTaskDTO extends BaseDTO {

    private static final long serialVersionUID = 7178941950947677060L;

    @Schema(description = "实体ID")
    private Long entityId;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
