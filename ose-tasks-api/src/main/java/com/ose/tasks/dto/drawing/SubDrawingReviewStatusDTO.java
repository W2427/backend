package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.drawing.DrawingReviewStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class SubDrawingReviewStatusDTO extends BaseDTO {

    @Schema(description = "校审状态")
    private DrawingReviewStatus reviewStatus;

    private List<Long> subDrawingList;

    public DrawingReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(DrawingReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public List<Long> getSubDrawingList() {
        return subDrawingList;
    }

    public void setSubDrawingList(List<Long> subDrawingList) {
        this.subDrawingList = subDrawingList;
    }
}
