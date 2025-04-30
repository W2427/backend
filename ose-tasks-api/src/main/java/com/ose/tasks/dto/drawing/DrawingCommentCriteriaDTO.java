package com.ose.tasks.dto.drawing;

import com.ose.dto.PageDTO;
import com.ose.tasks.vo.drawing.DrawingCoordinateType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 图纸评论列表查询DTO
 */
public class DrawingCommentCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -4123570150162436536L;

    @Schema(description = "查询关键词")
    private String keyword;

    private Long drawingId;

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
