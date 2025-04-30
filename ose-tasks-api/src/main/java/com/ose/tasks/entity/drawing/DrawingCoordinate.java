package com.ose.tasks.entity.drawing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.vo.drawing.DrawingCoordinateType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.List;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "drawing_coordinate")
public class DrawingCoordinate extends BaseBizEntity {

    private static final long serialVersionUID = 6608972868173687558L;
    @Schema(description = "组织 ID")
    private Long orgId;

    @Schema(description = "项目 ID")
    private Long projectId;

    @Schema(description = "坐标类型")
    @Enumerated(EnumType.STRING)
    private DrawingCoordinateType drawingCoordinateType;

    @Schema(description = "X坐标")
    private int drawingPositionX;

    @Schema(description = "Y坐标")
    private int drawingPositionY;
    @Schema(description = "条形码宽")
    private Integer graphWidth;
    @Schema(description = "条形码高")
    private Integer graphHeight;

    @Schema(description = "图纸封面宽")
    private Float drawingCoverWidth;

    @Schema(description = "图纸封面高")
    private Float drawingCoverHeight;

    @Schema(description = "坐标名称")
    private String name;

    private Long fileId;

    private String fileName;

    private String filePath;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "bpm_entity_type_coordinate_relation",
        joinColumns = @JoinColumn(name = "drawing_coordinate_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "entity_sub_type_id", referencedColumnName = "id"))
    private List<BpmEntitySubType> entitySubTypes;

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

    public void setDrawingCoverWidth(Float drawingCoverWidth) {
        this.drawingCoverWidth = drawingCoverWidth;
    }

    public void setDrawingCoverHeight(Float drawingCoverHeight) {
        this.drawingCoverHeight = drawingCoverHeight;
    }

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

    public DrawingCoordinateType getDrawingCoordinateType() {
        return drawingCoordinateType;
    }

    public void setDrawingCoordinateType(DrawingCoordinateType drawingCoordinateType) {
        this.drawingCoordinateType = drawingCoordinateType;
    }

    public Float getDrawingCoverWidth() {
        return drawingCoverWidth;
    }

    public Float getDrawingCoverHeight() {
        return drawingCoverHeight;
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

    public List<BpmEntitySubType> getEntitySubTypes() {
        return entitySubTypes;
    }

    @JsonIgnore
    public void setEntitySubTypes(List<BpmEntitySubType> entitySubTypes) {
        this.entitySubTypes = entitySubTypes;
    }
}
