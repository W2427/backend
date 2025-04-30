package com.ose.tasks.dto.taskpackage;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.drawing.DrawingType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 任务包-图纸创建数据传输对象。
 */
public class TaskPackageDrawingRelationCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 3801571917764633332L;

    @Schema(description = "图纸列表")
    @NotNull
    @Size(min = 1)
    @Valid
    private List<DrawingDTO> drawings;

    public List<DrawingDTO> getDrawings() {
        return drawings;
    }

    public void setDrawings(List<DrawingDTO> drawings) {
        this.drawings = drawings;
    }

    public static class DrawingDTO implements Serializable {

        private static final long serialVersionUID = 2889167290064084965L;

        @Schema(description = "图纸类型")
        @NotNull
        private DrawingType drawingType;

        @Schema(description = "图纸 ID")
        @NotNull
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

}
