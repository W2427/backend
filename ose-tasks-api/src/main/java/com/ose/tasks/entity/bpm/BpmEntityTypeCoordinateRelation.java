package com.ose.tasks.entity.bpm;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import org.springframework.data.annotation.Id;

import jakarta.persistence.*;

/**
 * 实体工序管理 实体类。
 */
@Entity
@Table(
    name = "bpm_entity_type_coordinate_relation",
    indexes = {
        @Index(columnList = "drawing_coordinate_id,status,entity_sub_type_id")
    }
)
public class BpmEntityTypeCoordinateRelation extends BaseBizEntity {

    private static final long serialVersionUID = -2328124483579797604L;
    //项目id
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    //组织id
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    // 实体id
    @Id
    @ManyToOne
    @JoinColumn(name = "entity_sub_type_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private BpmEntitySubType entitySubType;


    // 工序id
    @Id
    @ManyToOne
    @JoinColumn(name = "drawing_coordinate_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private DrawingCoordinate drawingCoordinate;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public BpmEntitySubType getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(BpmEntitySubType entitySubType) {
        this.entitySubType = entitySubType;
    }

    public DrawingCoordinate getDrawingCoordinate() {
        return drawingCoordinate;
    }

    public void setDrawingCoordinate(DrawingCoordinate drawingCoordinate) {
        this.drawingCoordinate = drawingCoordinate;
    }
}
