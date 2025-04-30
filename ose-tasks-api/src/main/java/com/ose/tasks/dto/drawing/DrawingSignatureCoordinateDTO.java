package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.drawing.DrawingSignatureType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class DrawingSignatureCoordinateDTO extends BaseDTO {

    private static final long serialVersionUID = 5971440931157335574L;

    @Schema(description = "电子签名类型")
    @Enumerated(EnumType.STRING)
    private DrawingSignatureType drawingSignatureType;

    @Schema(description = "X坐标")
    private int drawingPositionX;

    @Schema(description = "Y坐标")
    private int drawingPositionY;

    @Schema(description = "打印尺寸")
    private int drawingScaleToFit;

    public DrawingSignatureType getDrawingSignatureType() {
        return drawingSignatureType;
    }

    public void setDrawingSignatureType(DrawingSignatureType drawingSignatureType) {
        this.drawingSignatureType = drawingSignatureType;
    }

    public int getDrawingPositionX() {
        return drawingPositionX;
    }

    public void setDrawingPositionX(int drawingPositionX) {
        this.drawingPositionX = drawingPositionX;
    }

    public int getDrawingPositionY() {
        return drawingPositionY;
    }

    public void setDrawingPositionY(int drawingPositionY) {
        this.drawingPositionY = drawingPositionY;
    }

    public int getDrawingScaleToFit() {
        return drawingScaleToFit;
    }

    public void setDrawingScaleToFit(int drawingScaleToFit) {
        this.drawingScaleToFit = drawingScaleToFit;
    }
}
