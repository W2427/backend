package com.ose.tasks.dto.taskpackage;

import com.ose.dto.PageDTO;
import com.ose.tasks.vo.drawing.DrawingType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 任务包-图纸查询条件数据传输对象。
 */
public class TaskPackageDrawingRelationCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -394237608481652268L;

    @Schema(description = "图纸类型")
    private DrawingType drawingType;

    @Schema(description = "主图纸 ID")
    private Long drawingId;

    public DrawingType getDrawingType() {
        return drawingType;
    }

    public void setDrawingType(DrawingType drawingType) {
        this.drawingType = drawingType;
    }

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }
}
