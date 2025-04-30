package com.ose.tasks.dto.bpm;

import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmReDeployment;
import com.ose.tasks.entity.drawing.DrawingCoordinate;

import java.util.List;


/**
 * 工序管理 数据传输对象
 */
public class DrawingCoordinateResponseDTO extends DrawingCoordinate {

    private static final long serialVersionUID = -7422269895873450634L;
    /**
     *
     */

    private List<BpmEntitySubType> entitySubTypeList;

    public DrawingCoordinateResponseDTO(DrawingCoordinate drawingCoordinate) {
        if (drawingCoordinate != null) {
            this.entitySubTypeList = drawingCoordinate.getEntitySubTypes();

            this.setOrgId(drawingCoordinate.getOrgId());
            this.setProjectId(drawingCoordinate.getProjectId());

            this.setId(drawingCoordinate.getId());
            this.setStatus(drawingCoordinate.getStatus());
            this.setCreatedAt(drawingCoordinate.getCreatedAt());
            this.setLastModifiedAt(drawingCoordinate.getLastModifiedAt());

            this.setDrawingCoordinateType(drawingCoordinate.getDrawingCoordinateType());
            this.setDrawingPositionX(drawingCoordinate.getDrawingPositionX());
            this.setDrawingPositionY(drawingCoordinate.getDrawingPositionY());
            this.setGraphWidth(drawingCoordinate.getGraphWidth());
            this.setGraphHeight(drawingCoordinate.getGraphHeight());
            this.setDrawingCoverHeight(drawingCoordinate.getDrawingCoverHeight());
            this.setDrawingCoverWidth(drawingCoordinate.getDrawingCoverWidth());
            this.setName(drawingCoordinate.getName());
            this.setFileId(drawingCoordinate.getFileId());
            this.setFileName(drawingCoordinate.getFileName());
            this.setFilePath(drawingCoordinate.getFilePath());
        }
    }

    public List<BpmEntitySubType> getEntitySubTypeList() {
        return entitySubTypeList;
    }

    public void setEntitySubTypeList(List<BpmEntitySubType> entitySubTypeList) {
        this.entitySubTypeList = entitySubTypeList;
    }
}
