package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 生产设计图纸清单导入数据传输对象。
 */
public class DrawingImportDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "上传的导入文件的临时文件名")
    private String fileName;

    @Schema(description = "图纸类型")
    private String drawingType;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDrawingType() {
        return drawingType;
    }

    public void setDrawingType(String drawingType) {
        this.drawingType = drawingType;
    }
}
