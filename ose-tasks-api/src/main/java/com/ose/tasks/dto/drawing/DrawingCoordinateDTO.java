package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.drawing.DrawingCoordinateType;
import com.ose.tasks.vo.drawing.DrawingSignatureType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class DrawingCoordinateDTO extends BaseDTO {

    private static final long serialVersionUID = 5971440931157335574L;

    @Schema(description = "X坐标")
    private int drawingPositionX;

    @Schema(description = "Y坐标")
    private int drawingPositionY;

    @Schema(description = "实体类型 ID")
    private Long entitySubTypeId;

    @Schema(description = "实体类型")
    private String entitySubType;
    @Schema(description = "条形码宽")
    private Integer graphWidth;
    @Schema(description = "条形码高")
    private Integer graphHeight;

    @Schema(description = "图纸封面宽")
    private Float drawingCoverWidth;

    @Schema(description = "图纸封面高")
    private Float drawingCoverHeight;

    @Schema(description = "坐标类型")
    @Enumerated(EnumType.STRING)
    private DrawingCoordinateType drawingCoordinateType;

    @Schema(description = "坐标名称")
    private String name;

    private Long fileId;

    private String fileName;

    private String filePath;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGraphWidth() {
        return graphWidth;
    }

    public void setGraphWidth(Integer graphWidth) {
        this.graphWidth = graphWidth;
    }

    public Integer getGraphHeight() {
        return graphHeight;
    }

    public void setGraphHeight(Integer graphHeight) {
        this.graphHeight = graphHeight;
    }

    public DrawingCoordinateType getDrawingCoordinateType() {
        return drawingCoordinateType;
    }

    public void setDrawingCoordinateType(DrawingCoordinateType drawingCoordinateType) {
        this.drawingCoordinateType = drawingCoordinateType;
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

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public Float getDrawingCoverWidth() {
        return drawingCoverWidth;
    }

    public void setDrawingCoverWidth(Float drawingCoverWidth) {
        this.drawingCoverWidth = drawingCoverWidth;
    }

    public Float getDrawingCoverHeight() {
        return drawingCoverHeight;
    }

    public void setDrawingCoverHeight(Float drawingCoverHeight) {
        this.drawingCoverHeight = drawingCoverHeight;
    }
}
