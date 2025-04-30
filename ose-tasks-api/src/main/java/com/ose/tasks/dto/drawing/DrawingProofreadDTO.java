package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.drawing.DrawingReviewStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 */
public class DrawingProofreadDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "任务id,BT34BD123456")
    private Long taskId;

    @Schema(description = "任务流程id")
    private Long actInstId;

    @Schema(description = "子图纸设校审状态")
    private DrawingReviewStatus drawingReviewStatus;

    @Schema(description = "RedMark 校审指派人")
    private Long redMarkAssignId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public DrawingReviewStatus getDrawingReviewStatus() {
        return drawingReviewStatus;
    }

    public void setDrawingReviewStatus(DrawingReviewStatus drawingReviewStatus) {
        this.drawingReviewStatus = drawingReviewStatus;
    }

    public Long getRedMarkAssignId() {
        return redMarkAssignId;
    }

    public void setRedMarkAssignId(Long redMarkAssignId) {
        this.redMarkAssignId = redMarkAssignId;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }
}
