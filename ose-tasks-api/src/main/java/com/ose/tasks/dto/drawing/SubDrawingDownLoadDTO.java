package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 传输对象
 */
public class SubDrawingDownLoadDTO extends BaseDTO {

    private static final long serialVersionUID = 8343777030244268919L;

    @Schema(description = "主图纸版本")
    private String drawingVersion;

    @Schema(description = "子图纸状态")
    private EntityStatus  Status;

    public String getDrawingVersion() {
        return drawingVersion;
    }

    public void setDrawingVersion(String drawingVersion) {
        this.drawingVersion = drawingVersion;
    }

    public EntityStatus getStatus() {
        return Status;
    }

    public void setStatus(EntityStatus status) {
        Status = status;
    }
}
