package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;


/**
 * 图纸记录
 */
@Entity
@Table(name = "drawing_project_dept_idc_relation",
    indexes = {
        @Index(columnList = "projectId,drawingId,departmentId,deleted"),
        @Index(columnList = "drawingId")
    })
public class DrawingProjectDeptIDCRelation extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 3396533506473962706L;
    @Schema(description = "组织Id")
    private Long orgId;

    @Schema(description = "项目Id")
    private Long projectId;

    @Schema(description = "部门ID")
    private Long departmentId;

    @Schema(description = "图纸Id")
    private Long drawingId;

    @Schema(description = "工程师Id")
    private Long userId;

    @Schema(description = "专业")
    private String discipline;

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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
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

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
