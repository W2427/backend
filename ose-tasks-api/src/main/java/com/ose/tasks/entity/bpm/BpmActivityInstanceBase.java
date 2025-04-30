package com.ose.tasks.entity.bpm;

import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 任务 基础表，存储基础信息
 */
@Entity
@Table(name = "bpm_activity_instance_base",
    indexes = {
        @Index(columnList = "projectId,entityId,process_name,status"),
        @Index(columnList = "projectId,process_stage_id,process_category_id,entity_sub_type_id,process_id"),
        @Index(columnList = "projectId,entityNo"),
        @Index(columnList = "projectId,entityNo,process_name,process_stage_name"),
        @Index(columnList = "orgId,projectId,id")
    })
public class BpmActivityInstanceBase extends BaseBizEntity {


    private static final long serialVersionUID = -3382142822778391762L;
    //项目id
    @Column
    private Long projectId;

    //组织id
    @Column
    private Long orgId;

    @Schema(description = "版本")
    private String version;


    @Schema(description = "BPMN版本")
    private int bpmnVersion;

    @Schema(description = "实体编号")
    @Column
    private String entityNo;

    @Schema(description = "实体编号1")
    @Column
    private String entityNo1;

    @Schema(description = "实体编号2")
    @Column
    private String entityNo2;

    @Column(name = "drawing_title")
    private String drawingTitle;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entityId")
    private Long entityId;

    @Transient
    private String taskDefKey;

    @Schema(description = "实体NodeID")
    @Column(name = "entity_project_node_id")
    private Long entityProjectNodeId;

    // 实体层级模块名
    @Column(name = "entity_module_name")
    private String entityModuleName;

    @Schema(description = "实体层级模块 node ID")
    @Column(name = "entity_module_project_node_id")
    private Long entityModuleProjectNodeId;

    @Schema(description = "实体类型分类id")
    @Column(name = "entity_type_id")
    private Long entityTypeId;

    @Schema(description = "实体类型")
    @Column(name = "entity_sub_type")
    private String entitySubType;

    @Schema(description = "实体类型id")
    @Column(name = "entity_sub_type_id")
    private Long entitySubTypeId;

    @Schema(description = "工序分类")
    @Column(name = "process_stage_name")
    private String processStage;

    @Schema(description = "工序分类id")
    @Column(name = "process_stage_id")
    private Long processStageId;

    @Schema(description = "工序")
    @Column(name = "process_name")
    private String process;

    @Schema(description = "工序id")
    @Column(name = "process_id")
    private Long processId;

    @Schema(description = "流程担当者id")
    @Column(name = "owner_id")
    private Long ownerId;

    @Schema(description = "流程担当者名")
    @Column(name = "owner_name")
    private String ownerName;

    @Schema(description = "流程分配者id")
    @Column(name = "allocatee")
    private Long allocatee;

    @Schema(description = "分配时间")
    @Column(name = "allocatee_date")
    private Date allocateeDate;

    @Schema(description = "计划开始时间")
    @Column(name = "plan_start_date")
    private Date planStartDate;

    @Schema(description = "计划结束时间")
    @Column(name = "plan_end_date")
    private Date planEndDate;

    @Schema(description = "计划开始时间")
    @Column(name = "task_package_plan_start_date")
    private Date taskPackagePlanStartDate;

    @Schema(description = "计划结束时间")
    @Column(name = "task_package_plan_end_date")
    private Date taskPackagePlanEndDate;

    @Schema(description = "计划工时")
    @Column(name = "plan_hour")
    private Double planHour;

    @Schema(description = "流程类型")
    @Column(name = "act_category")
    private String actCategory;

    @Schema(description = "工序分类id")
    @Column(name = "process_category_id")
    private Long processCategoryId;

    @Schema(description = "实际开始时间")
    @Transient
    private Date startDate;

    @Schema(description = "排序字段")
    private Integer sortNo;

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

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public String getProcessStage() {
        return processStage;
    }

    public void setProcessStage(String processStage) {
        this.processStage = processStage;
    }

    public Long getProcessStageId() {
        return processStageId;
    }

    public void setProcessStageId(Long processStageId) {
        this.processStageId = processStageId;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getAllocatee() {
        return allocatee;
    }

    public void setAllocatee(Long allocatee) {
        this.allocatee = allocatee;
    }

    public Date getAllocateeDate() {
        return allocateeDate;
    }

    public void setAllocateeDate(Date allocateeDate) {
        this.allocateeDate = allocateeDate;
    }

    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }

    public Date getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(Long entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public String getActCategory() {
        return actCategory;
    }

    public void setActCategory(String actCategory) {
        this.actCategory = actCategory;
    }

    public String getEntityModuleName() {
        return entityModuleName;
    }

    public void setEntityModuleName(String entityModuleName) {
        this.entityModuleName = entityModuleName;
    }

    public Long getEntityModuleProjectNodeId() {
        return entityModuleProjectNodeId;
    }

    public void setEntityModuleProjectNodeId(Long entityModuleProjectNodeId) {
        this.entityModuleProjectNodeId = entityModuleProjectNodeId;
    }

    public Long getEntityProjectNodeId() {
        return entityProjectNodeId;
    }

    public void setEntityProjectNodeId(Long entityProjectNodeId) {
        this.entityProjectNodeId = entityProjectNodeId;
    }

    public Long getProcessCategoryId() {
        return processCategoryId;
    }

    public void setProcessCategoryId(Long processCategoryId) {
        this.processCategoryId = processCategoryId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Double getPlanHour() {
        return planHour;
    }

    public void setPlanHour(Double planHour) {
        this.planHour = planHour;
    }

    public String getDrawingTitle() {
        return drawingTitle;
    }

    public void setDrawingTitle(String drawingTitle) {
        this.drawingTitle = drawingTitle;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getBpmnVersion() {
        return bpmnVersion;
    }

    public void setBpmnVersion(int bpmnVersion) {
        this.bpmnVersion = bpmnVersion;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Date getTaskPackagePlanStartDate() {
        return taskPackagePlanStartDate;
    }

    public void setTaskPackagePlanStartDate(Date taskPackagePlanStartDate) {
        this.taskPackagePlanStartDate = taskPackagePlanStartDate;
    }

    public Date getTaskPackagePlanEndDate() {
        return taskPackagePlanEndDate;
    }

    public void setTaskPackagePlanEndDate(Date taskPackagePlanEndDate) {
        this.taskPackagePlanEndDate = taskPackagePlanEndDate;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getEntityNo1() {
        return entityNo1;
    }

    public void setEntityNo1(String entityNo1) {
        this.entityNo1 = entityNo1;
    }

    public String getEntityNo2() {
        return entityNo2;
    }

    public void setEntityNo2(String entityNo2) {
        this.entityNo2 = entityNo2;
    }
}
