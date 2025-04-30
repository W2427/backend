package com.ose.tasks.dto.drawing;

import java.util.Date;

import com.ose.dto.BaseDTO;

/**
 * 实体管理 数据传输对象
 */
public class DrawingTaskDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    // 图号
    private String dwgNo;

    // 模型id
    private String modelName;

    // 委托者
    private Long assignee;

    // 委托者名字
    private String assigneeName;

    // 预计开始日期
    private Date planStart;

    // 预计结束日期
    private Date planEnd;

    private Long entityId;

    private String drawingCategory;

    private Long drawingCategoryId;

    private String drawingCategoryType;

    private Long drawingCategoryTypeId;

    // 预计工时
    private Double planHour = 0.0;


    public String getDwgNo() {
        return dwgNo;
    }

    public void setDwgNo(String dwgNo) {
        this.dwgNo = dwgNo;
    }

    public Long getAssignee() {
        return assignee;
    }

    public void setAssignee(Long assigneeId) {
        this.assignee = assigneeId;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public Date getPlanStart() {
        return planStart;
    }

    public void setPlanStart(Date planStart) {
        this.planStart = planStart;
    }

    public Date getPlanEnd() {
        return planEnd;
    }

    public void setPlanEnd(Date planEnd) {
        this.planEnd = planEnd;
    }

    public Double getPlanHour() {
        return planHour;
    }

    public void setPlanHour(Double planHour) {
        this.planHour = planHour;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDrawingCategory() {
        return drawingCategory;
    }

    public void setDrawingCategory(String drawingCategory) {
        this.drawingCategory = drawingCategory;
    }

    public Long getDrawingCategoryId() {
        return drawingCategoryId;
    }

    public void setDrawingCategoryId(Long drawingCategoryId) {
        this.drawingCategoryId = drawingCategoryId;
    }

    public String getDrawingCategoryType() {
        return drawingCategoryType;
    }

    public void setDrawingCategoryType(String drawingCategoryType) {
        this.drawingCategoryType = drawingCategoryType;
    }

    public Long getDrawingCategoryTypeId() {
        return drawingCategoryTypeId;
    }

    public void setDrawingCategoryTypeId(Long drawingCategoryTypeId) {
        this.drawingCategoryTypeId = drawingCategoryTypeId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

}
