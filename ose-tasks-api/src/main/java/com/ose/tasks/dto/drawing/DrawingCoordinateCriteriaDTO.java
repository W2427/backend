package com.ose.tasks.dto.drawing;

import com.ose.dto.PageDTO;
import com.ose.tasks.vo.drawing.DrawingCoordinateType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 图纸坐标列表查询DTO
 */
public class DrawingCoordinateCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -4123570150162436536L;

    @Schema(description = "坐标类型")
    private DrawingCoordinateType drawingCoordinateType;

    @Schema(description = "名称")
    private String name;

    public DrawingCoordinateType getDrawingCoordinateType() {
        return drawingCoordinateType;
    }

    public void setDrawingCoordinateType(DrawingCoordinateType drawingCoordinateType) {
        this.drawingCoordinateType = drawingCoordinateType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
