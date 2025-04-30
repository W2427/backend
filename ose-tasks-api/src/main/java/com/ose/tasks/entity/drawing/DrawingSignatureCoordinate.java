package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.drawing.DrawingSignatureType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "drawing_signature_coordinate")
public class DrawingSignatureCoordinate extends BaseBizEntity {

    @Schema(description = "组织 ID")
    private Long orgId;

    @Schema(description = "项目 ID")
    private Long projectId;

    @Schema(description = "实体类型 ID")
    private Long entitySubTypeId;

    @Schema(description = "电子签名类型")
    @Enumerated(EnumType.STRING)
    private DrawingSignatureType drawingSignatureType;

    @Schema(description = "X坐标")
    private int drawingPositionX;

    @Schema(description = "Y坐标")
    private int drawingPositionY;

    @Schema(description = "打印尺寸")
    private int drawingScaleToFit;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

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
