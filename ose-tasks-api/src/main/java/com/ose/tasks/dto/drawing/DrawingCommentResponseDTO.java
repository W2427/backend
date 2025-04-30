package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class DrawingCommentResponseDTO extends BaseDTO {

    private static final long serialVersionUID = 8547937155313823732L;
    @Schema(description = "评论ID")
    private Long drawingCommentId;

    private Long ruTaskId;

    private String content;

    public Long getRuTaskId() {
        return ruTaskId;
    }

    public void setRuTaskId(Long ruTaskId) {
        this.ruTaskId = ruTaskId;
    }

    public Long getDrawingCommentId() {
        return drawingCommentId;
    }

    public void setDrawingCommentId(Long drawingCommentId) {
        this.drawingCommentId = drawingCommentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
