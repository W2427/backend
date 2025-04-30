package com.ose.tasks.entity.drawing;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.drawing.DrawingVariableType;

/**
 * 子图纸
 */
@Entity
@Table(name = "sub_drawing_config")
public class SubDrawingConfig extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -7837891255273680991L;


    private Long orgId;

    private Long projectId;

    //图纸分类id
    private Long drawingCategoryId;

    //变量名称
    private String variableName;

    //变量名称
    private String dispalyName;

    //变量类型
    @Enumerated(EnumType.STRING)
    private DrawingVariableType variableType;

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

    public Long getDrawingCategoryId() {
        return drawingCategoryId;
    }

    public void setDrawingCategoryId(Long drawingCategoryId) {
        this.drawingCategoryId = drawingCategoryId;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public DrawingVariableType getVariableType() {
        return variableType;
    }

    public void setVariableType(DrawingVariableType variableType) {
        this.variableType = variableType;
    }

    public String getDispalyName() {
        return dispalyName;
    }

    public void setDispalyName(String dispalyName) {
        this.dispalyName = dispalyName;
    }


}
