package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 */
public class DrawingProjectDeptIDCRelationDTO extends BaseDTO {

    private static final long serialVersionUID = -650925449509394613L;
    @Schema(description = "图纸ID")
    private Long drawingId;

    @Schema(description = "部门ID")
    private Long departmentId;

    @Schema(description = "部门ID")
    private Long userId;

    @Schema(description = "专业")
    private String discipline;

    @Schema(description = "用户名称")
    private String username;

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
