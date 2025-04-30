package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.io.Serial;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "drawing_plan_hour")
public class DrawingPlanHour extends BaseEntity {

    private static final long serialVersionUID = -90078809908124971L;
    @Schema(description = "项目 ID")
    private Long projectId;

    @Schema(description = "图纸 ID")
    private Long drawingId;

    private Long userId;

    private String privilege;

    private Integer monthly;

    private Double hours;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public Integer getMonthly() {
        return monthly;
    }

    public void setMonthly(Integer monthly) {
        this.monthly = monthly;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }
}
