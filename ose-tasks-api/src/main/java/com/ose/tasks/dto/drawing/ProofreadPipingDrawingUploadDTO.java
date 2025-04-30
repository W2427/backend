package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 传输对象
 */
public class ProofreadPipingDrawingUploadDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "临时文件名")
    private String fileName;

    @Schema(description = "临时文件名")
    private Long subDrawingId;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getSubDrawingId() {
        return subDrawingId;
    }

    public void setSubDrawingId(Long subDrawingId) {
        this.subDrawingId = subDrawingId;
    }
}
