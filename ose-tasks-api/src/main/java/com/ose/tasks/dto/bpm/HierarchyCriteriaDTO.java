package com.ose.tasks.dto.bpm;

import com.ose.dto.PageDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 任务查询条件数据传输对象类。
 */
public class HierarchyCriteriaDTO extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "工序分类id")
    private Long processCategoryId;

    @Schema(description = "实体类型分类id")
    private Long entityTypeId;

    @Schema(description = "实体类型id")
    private Long entitySubTypeId;

    @Schema(description = "工序分类id")
    private Long processStageId;

    @Schema(description = "工序id")
    private Long processId;

    @Schema(description = "任务节点")
    private String taskNode;

    @Schema(description = "模块")
    private String entityModuleNames;

    @Schema(description = "批量处理标识")
    private Boolean batchFlag = false;

    @Schema(description = "任务节点驱动筛选标识")
    private Boolean nodeFlag = false;

    @Schema(description = "任务节点defKey")
    private String taskDefKey;

    @Schema(description = "NDT状态")
    private String genType;

    private  String discipline;

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public Long getProcessStageId() {
        return processStageId;
    }

    public void setProcessStageId(Long processStageId) {
        this.processStageId = processStageId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(Long entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public Long getProcessCategoryId() {
        return processCategoryId;
    }

    public void setProcessCategoryId(Long processCategoryId) {
        this.processCategoryId = processCategoryId;
    }

    public String getTaskNode() {
        return taskNode;
    }

    public void setTaskNode(String taskNode) {
        this.taskNode = taskNode;
    }

    public String getEntityModuleNames() {
        return entityModuleNames;
    }

    public void setEntityModuleNames(String entityModuleNames) {
        this.entityModuleNames = entityModuleNames;
    }

    public Boolean getBatchFlag() {
        return batchFlag;
    }

    public void setBatchFlag(Boolean batchFlag) {
        this.batchFlag = batchFlag;
    }

    public Boolean getNodeFlag() {
        return nodeFlag;
    }

    public void setNodeFlag(Boolean nodeFlag) {
        this.nodeFlag = nodeFlag;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getGenType() {
        return genType;
    }

    public void setGenType(String genType) {
        this.genType = genType;
    }
}
