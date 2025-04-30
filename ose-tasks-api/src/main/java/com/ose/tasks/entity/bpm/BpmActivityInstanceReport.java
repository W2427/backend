package com.ose.tasks.entity.bpm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseEntity;
import com.ose.tasks.dto.qc.ReportSubTypeDTO;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.tasks.vo.qc.NDEType;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bpm_activity_instance_report") //view 视图
public class BpmActivityInstanceReport extends BaseEntity {

    private static final long serialVersionUID = 4395905153985336518L;

    //项目id
    private Long projectId;

    //组织id
    private Long orgId;

    @Column
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    //工作流实例id
    private Long actInstId;

    @Schema(description = "版本")
    private String version;

    @Schema(description = "实体编号")
    private String entityNo;

    private String drawingTitle;

    private Long entityId;

//    private String taskDefKey;

    @Schema(description = "实体NodeID")
    private Long entityProjectNodeId;

    // 实体层级模块名
    private String entityModuleName;

    @Schema(description = "实体层级模块 node ID")
    private Long entityModuleProjectNodeId;

    @Schema(description = "实体类型分类")
    private String entityType;

    @Schema(description = "实体类型分类id")
    private Long entityTypeId;

    @Schema(description = "实体类型")
    private String entitySubType;

    @Schema(description = "实体类型id")
    private Long entitySubTypeId;

    @Schema(description = "工序分类")
    private String processStage;

    @Schema(description = "工序分类id")
    private Long processStageId;

    @Schema(description = "工序")
    private String process;

    @Schema(description = "工序id")
    private Long processId;

    @Schema(description = "流程类型")
    private String actCategory;

    @Schema(description = "工序分类id")
    private Long processCategoryId;

    @Schema(description = "管线等级")
    private String pipeClass;

    @Schema(description = "NDT类型")
    @Enumerated(EnumType.STRING)
    private NDEType ndeType;

    @Schema(description = "焊口材料分组代码")
    private String materialGroupCode;

    @Schema(description = "正在运行的任务节点执行人")
    private String currentExecutor;

    @Schema(description = "子报告 主报告关系")
    private String reportSubTypeInfo;

    @Schema(description = "焊口返修次数")
    @Column(columnDefinition = "INT default 0")
    private Integer weldRepairCount = 0;

    @Schema(description = "工作场地名称")
    private String workSiteName;

    @Schema(description = "WPS信息")
    @Column(name = "wps_no")
    private String wpsNo;

    @Schema(description = "挂起状态")
    @Enumerated(EnumType.STRING)
    private SuspensionState suspensionState;

    @Schema(description = "完成状态")
    @Enumerated(EnumType.STRING)
    private ActInstFinishState finishState;

    private String oldReportNo;

    @Schema(description = "rt、ut任务数量")
    private Integer rtUtCount;

    @Schema(description = "ut任务数量")
    private Integer utCount;

    @Schema(description = "是否为paut")
    private boolean isPaut = false;

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

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getDrawingTitle() {
        return drawingTitle;
    }

    public void setDrawingTitle(String drawingTitle) {
        this.drawingTitle = drawingTitle;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

//    public String getTaskDefKey() {
//        return taskDefKey;
//    }
//
//    public void setTaskDefKey(String taskDefKey) {
//        this.taskDefKey = taskDefKey;
//    }

    public Long getEntityProjectNodeId() {
        return entityProjectNodeId;
    }

    public void setEntityProjectNodeId(Long entityProjectNodeId) {
        this.entityProjectNodeId = entityProjectNodeId;
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

    public Long getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(Long entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
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

    public String getActCategory() {
        return actCategory;
    }

    public void setActCategory(String actCategory) {
        this.actCategory = actCategory;
    }

    public Long getProcessCategoryId() {
        return processCategoryId;
    }

    public void setProcessCategoryId(Long processCategoryId) {
        this.processCategoryId = processCategoryId;
    }

    public String getPipeClass() {
        return pipeClass;
    }

    public void setPipeClass(String pipeClass) {
        this.pipeClass = pipeClass;
    }

    public NDEType getNdeType() {
        return ndeType;
    }

    public void setNdeType(NDEType ndeType) {
        this.ndeType = ndeType;
    }

    public String getMaterialGroupCode() {
        return materialGroupCode;
    }

    public void setMaterialGroupCode(String materialGroupCode) {
        this.materialGroupCode = materialGroupCode;
    }

    public String getCurrentExecutor() {
        return currentExecutor;
    }

    public void setCurrentExecutor(String currentExecutor) {
        this.currentExecutor = currentExecutor;
    }

    public String getReportSubTypeInfo() {
        return reportSubTypeInfo;
    }

    public void setReportSubTypeInfo(String reportSubTypeInfo) {
        this.reportSubTypeInfo = reportSubTypeInfo;
    }

    @JsonProperty(value = "reportSubTypeInfo", access = JsonProperty.Access.READ_ONLY)
    public List<ReportSubTypeDTO> getJsonReportSubTypeInfo() {
        if (reportSubTypeInfo != null && !"".equals(reportSubTypeInfo)) {
            return StringUtils.decode(reportSubTypeInfo, new TypeReference<List<ReportSubTypeDTO>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonReportSubTypeInfo(List<ReportSubTypeDTO> reportSubTypeInfo) {
        if (reportSubTypeInfo != null) {
            this.reportSubTypeInfo = StringUtils.toJSON(reportSubTypeInfo);
        }
    }

    public Integer getWeldRepairCount() {
        return weldRepairCount;
    }

    public void setWeldRepairCount(Integer weldRepairCount) {
        this.weldRepairCount = weldRepairCount;
    }

    public String getWorkSiteName() {
        return workSiteName;
    }

    public void setWorkSiteName(String workSiteName) {
        this.workSiteName = workSiteName;
    }

    public String getWpsNo() {
        return wpsNo;
    }

    public void setWpsNo(String wpsNo) {
        this.wpsNo = wpsNo;
    }

    public SuspensionState getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(SuspensionState suspensionState) {
        this.suspensionState = suspensionState;
    }

    public ActInstFinishState getFinishState() {
        return finishState;
    }

    public void setFinishState(ActInstFinishState finishState) {
        this.finishState = finishState;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public String getOldReportNo() {
        return oldReportNo;
    }

    public void setOldReportNo(String oldReportNo) {
        this.oldReportNo = oldReportNo;
    }

    public boolean isPaut() {
        return isPaut;
    }

    public void setPaut(boolean paut) {
        isPaut = paut;
    }

    public Integer getRtUtCount() {
        return rtUtCount;
    }

    public void setRtUtCount(Integer rtUtCount) {
        this.rtUtCount = rtUtCount;
    }

    public Integer getUtCount() {
        return utCount;
    }

    public void setUtCount(Integer utCount) {
        this.utCount = utCount;
    }
}
