package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 生产设计图纸详情传输对象。
 */
public class DrawingDetailCriteriaDTO extends BaseDTO {


    private static final long serialVersionUID = -4658965444877659928L;
    @Schema(description = "图纸序号")
    private Long drawingId;

    @Schema(description = "版本")
    private String RevNo;

    @Schema(description = "流程")
    private String process;

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public String getRevNo() {
        return RevNo;
    }

    public void setRevNo(String revNo) {
        RevNo = revNo;
    }
}
