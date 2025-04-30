package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 */
public class DrawingProjectDeptIDCRelationSearchDTO extends BaseDTO {

    private static final long serialVersionUID = -650925449509394613L;
    @Schema(description = "图纸ID")
    private String drawingId;

    @Schema(description = "部门ID")
    private String departmentId;

    @Schema(description = "专业")
    private String discipline;

    public String getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(String drawingId) {
        this.drawingId = drawingId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
