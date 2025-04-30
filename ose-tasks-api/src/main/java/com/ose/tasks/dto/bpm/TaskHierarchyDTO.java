package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 待办任务条件 数据传输对象
 */
public class TaskHierarchyDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = -34948993806220141L;

    @Schema(description = "工序分类")
    private List<HierarchyBaseDTO> processCategories;

    @Schema(description = "实体分类类型")
    private List<HierarchyBaseDTO> entityCategoryTypes;

    @Schema(description = "实体分类")
    private List<HierarchyBaseDTO> entityCategories;

    @Schema(description = "工序阶段")
    private List<HierarchyBaseDTO> processStages;

    @Schema(description = "工序")
    private List<HierarchyBaseDTO> processes;

    @Schema(description = "任务节点(筛选)")
    private List<HierarchyBaseDTO> taskDefKey;

    @Schema(description = "任务节点")
    private List<TaskNodeDTO> tasks;

    @Schema(description = "任务节点")
    private List<String> taskNodes;

    @Schema(description = "模块")
    private List<String> moduleNames;

    @Schema(description = "初始报告号")
    private List<String> oldReportNos;

    @Schema(description = "工序阶段-工序")
    private List<HierarchyStageProcessDTO> stageProcesses;

    public List<String> getTaskNodes() {
        return taskNodes;
    }

    public void setTaskNodes(List<String> taskNodes) {
        this.taskNodes = taskNodes;
    }

    public List<HierarchyBaseDTO> getEntityCategories() {
        return entityCategories;
    }

    public void setEntityCategories(List<HierarchyBaseDTO> entityCategories) {
        this.entityCategories = entityCategories;
    }

    public List<HierarchyBaseDTO> getProcessStages() {
        return processStages;
    }

    public void setProcessStages(List<HierarchyBaseDTO> processStages) {
        this.processStages = processStages;
    }

    public List<HierarchyBaseDTO> getProcesses() {
        return processes;
    }

    public void setProcesses(List<HierarchyBaseDTO> processes) {
        this.processes = processes;
    }

    public List<HierarchyBaseDTO> getEntityTypes() {
        return entityCategoryTypes;
    }

    public void setEntityTypes(List<HierarchyBaseDTO> entityCategoryTypes) {
        this.entityCategoryTypes = entityCategoryTypes;
    }

    public List<HierarchyBaseDTO> getProcessCategories() {
        return processCategories;
    }

    public void setProcessCategories(List<HierarchyBaseDTO> processCategories) {
        this.processCategories = processCategories;
    }

    public List<String> getModuleNames() {
        return moduleNames;
    }

    public void setModuleNames(List<String> moduleNames) {
        this.moduleNames = moduleNames;
    }

    public List<TaskNodeDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskNodeDTO> tasks) {
        this.tasks = tasks;
    }

    public List<HierarchyStageProcessDTO> getStageProcesses() {
        return stageProcesses;
    }

    public void setStageProcesses(List<HierarchyStageProcessDTO> stageProcesses) {
        this.stageProcesses = stageProcesses;
    }

    public List<HierarchyBaseDTO> getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(List<HierarchyBaseDTO> taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public List<String> getOldReportNos() {
        return oldReportNos;
    }

    public void setOldReportNos(List<String> oldReportNos) {
        this.oldReportNos = oldReportNos;
    }
}
