package com.ose.tasks.entity.bpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.tasks.dto.qc.ReportSubTypeDTO;
import com.ose.vo.InspectParty;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.tasks.vo.qc.InspectionFirstPassYield;
import com.ose.tasks.vo.qc.NDEType;
import com.ose.tasks.vo.SuspensionState;
import com.ose.util.StringUtils;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 任务
 */
@Entity
@Table(name = "bpm_activity_instance",
    indexes = {
        @Index(columnList = "org_id,project_id")
    })
public class BpmActivityInstanceOld extends BaseBizEntity {

    private static final long serialVersionUID = 6182161986526777812L;

    public BpmActivityInstanceOld() {

    }

    @JsonCreator
    public BpmActivityInstanceOld(@JsonProperty("reportSubTypeInfo") List<ReportSubTypeDTO> reportSubTypeInfo,
                                  @JsonProperty("reports") List<ActReportDTO> reports,
                                  @JsonProperty("inspectParties") List<InspectParty> inspectParties) {

        this.reports = StringUtils.toJSON(reports);
        this.reportSubTypeInfo = StringUtils.toJSON(reportSubTypeInfo);
        this.inspectParties = StringUtils.toJSON(inspectParties);

    }

    //项目id
    @Column(name = "project_id")
    private Long projectId;

    //组织id
    @Column(name = "org_id")
    private Long orgId;

    //工作流实例id
    @Column(name = "act_inst_id")
    @JsonIgnore
    private Long actInstId;

    @Schema(description = "版本")
    private String version;

    @Schema(description = "WPS信息")
    @Column(name = "wps_no")
    private String wpsNo;

    @Schema(description = "备注")
    private String memo;

    @Schema(description = "实体编号")
    @Column(name = "entity_no")
    private String entityNo;

    @Column(name = "drawing_title")
    private String drawingTitle;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_id")
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
    @Column(name = "process_stage_name_cn")
    private String processStage;

    @Schema(description = "工序分类id")
    @Column(name = "process_stage_id")
    private Long processStageId;

    @Schema(description = "工序")
    @Column(name = "a.process_name")
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

    @Schema(description = "计划工时")
    @Column(name = "plan_hour")
    private Double planHour;

    @Schema(description = "实际开始时间")
    @Column(name = "start_date")
    private Date startDate;

    @Schema(description = "实际结束时间")
    @Column(name = "end_date")
    private Date endDate;

    @Schema(description = "工时")
    @Column(name = "cost_hour")
    private Double costHour;

    @Schema(description = "挂起状态")
    @Enumerated(EnumType.STRING)
    private SuspensionState suspensionState;

    @Schema(description = "完成状态")
    @Enumerated(EnumType.STRING)
    private ActInstFinishState finishState;

    @Schema(description = "流程类型")
    @Column(name = "act_category")
    private String actCategory;

    @Schema(description = "工序分类id")
    @Column(name = "process_category_id")
    private Long processCategoryId;

    @Schema(description = "无损探伤比例")
    @Column(nullable = true, length = 16)
    private Integer ndeRatio;

    @Schema(description = "pmi比例")
    @Column(nullable = true, length = 16)
    private Integer pmiRatio;

    @Schema(description = "管线等级")
    private String pipeClass;

    @Schema(description = "NDT类型")
    @Enumerated(EnumType.STRING)
    private NDEType ndeType;

    @Schema(description = "焊口材料分组代码")
    @Column(nullable = true, length = 128)
    private String materialGroupCode;

    @Schema(description = "内检时间")
    private Date internalInspectionTime;

    @Schema(description = "外检时间")
    private Date externalInspectionTime;

    @Schema(description = "内检时间")
    private Date weldCompleteTime;

    @Schema(description = "内检第一次通过")
    @Enumerated(EnumType.ORDINAL)
    private InspectionFirstPassYield internalInspectionFpy;

    @Schema(description = "外检第一次通过")
    @Enumerated(EnumType.ORDINAL)
    private InspectionFirstPassYield externalInspectionFpy;

    @Schema(description = "报告内容")
    @Column(columnDefinition = "text")
    private String reports;

    @Schema(description = "外检遗留问题id")
    @Column(columnDefinition = "text")
    private String exInsIssueIds;

    @Schema(description = "内检遗留问题id")
    @Column(columnDefinition = "text")
    private String inInsIssueIds;

    @Schema(description = "焊工的焊口个数")
    @Column(nullable = true, columnDefinition = "INT default 0")
    private Integer weldWelderCount;

    @Schema(description = "执行错误flag")
    private boolean executeNgFlag = false;

    @Schema(description = "正在运行的任务节点执行人")
    private String currentExecutor;

    @Schema(description = "工作场地 ID")
    private Long workSiteId;

    @Schema(description = "工作场地名称")
    private String workSiteName;

    @Schema(description = "工作场地地址")
    private String workSiteAddress;

    @Schema(description = "工作班组")
    private String teamName;

    @Schema(description = "工作班组ID")
    private Long teamId;

    @Schema(description = "任务包名称")
    private String taskPackageName;

    @Schema(description = "任务包ID")
    private Long taskPackageId;

    @Schema(description = "spool实体ID")
    private Long spoolEntityId;

    @Schema(description = "spool号")
    private String spoolNo;

    @Transient
    @Schema(description = "焊接次数")
    private Integer weldCount = 0;

    @Schema(description = "流程内驳回次数")
    @Column(columnDefinition = "INT default 0")
    private int unAcceptCount = 0;

    @Schema(description = "焊口返修次数")
    @Column(columnDefinition = "INT default 0")
    private Integer weldRepairCount = 0;

    @Schema(description = "焊口扩口次数")
    @Column(columnDefinition = "INT default 0")
    private Integer weldPenaltyCount = 0;

    @Column(length = 100)
    @Schema(description = "检验方")
    private String inspectParties;

    @Schema(description = "当前任务节点")
    @Transient
    private String currentTaskNode;

    @Schema(description = "当前任务节点")
    @Transient
    private Date currentTaskNodeDate;

    @Column
    @Schema(description = "子报告 主报告关系")
    private String reportSubTypeInfo;

    @Column
    @Schema(description = "ndt是否完成")
    private String isNdtFinished;

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

    @JsonProperty(value = "exInsIssueIds", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonExInsIssueIdsReadOnly() {
        if (exInsIssueIds != null && !"".equals(exInsIssueIds)) {
            return StringUtils.decode(exInsIssueIds, new TypeReference<List<String>>() {
            });
        } else {
            return null;
        }
    }

    @JsonIgnore
    public void setJsonExInsIssueIds(List<String> exInsIssueIds) {
        if (exInsIssueIds != null) {
            this.exInsIssueIds = StringUtils.toJSON(exInsIssueIds);
        }
    }

    @JsonProperty(value = "inInsIssueIds", access = JsonProperty.Access.READ_ONLY)
    public List<Long> getJsonInInsIssueIdsReadOnly() {
        if (inInsIssueIds != null && !"".equals(inInsIssueIds)) {
            return StringUtils.decode(inInsIssueIds, new TypeReference<List<Long>>() {
            });
        } else {
            return null;
        }
    }

    @JsonIgnore
    public void setJsonInInsIssueIds(List<Long> inInsIssueIds) {
        if (inInsIssueIds != null) {
            this.inInsIssueIds = StringUtils.toJSON(inInsIssueIds);
        }
    }

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

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public SuspensionState getSuspensionState() {
        return suspensionState;
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

    public Date getInternalInspectionTime() {
        return internalInspectionTime;
    }

    public void setInternalInspectionTime(Date internalInspectionTime) {
        this.internalInspectionTime = internalInspectionTime;
    }

    public Date getExternalInspectionTime() {
        return externalInspectionTime;
    }

    public void setExternalInspectionTime(Date externalInspectionTime) {
        this.externalInspectionTime = externalInspectionTime;
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

    public String getReports() {
        return reports;
    }

    public void setReports(String reports) {
        this.reports = reports;
    }

    @JsonIgnore
    public void setJsonReports(List<ActReportDTO> reports) {
        if (reports != null) {
            this.reports = StringUtils.toJSON(reports);
        }
    }

    @JsonIgnore
    public void addJsonReports(ActReportDTO report) {
        if (report != null) {
            List<ActReportDTO> reportList = getJsonReportsReadOnly();
            for (int i = reportList.size() - 1; i >= 0; i--) {
                if (reportList.get(i).getReportQrCode().equals(report.getReportQrCode())) {
                    reportList.remove(i);
                }
            }
            reportList.add(report);
            this.reports = StringUtils.toJSON(reportList);
        }
    }

    @JsonProperty(value = "reports", access = JsonProperty.Access.READ_ONLY)
    public List<ActReportDTO> getJsonReportsReadOnly() {
        if (reports != null && !"".equals(reports)) {
            return StringUtils.decode(reports, new TypeReference<List<ActReportDTO>>() {
            });
        } else {
            return new ArrayList<>();
        }
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getExInsIssueIds() {
        return exInsIssueIds;
    }

    public void setExInsIssueIds(String exInsIssueIds) {
        this.exInsIssueIds = exInsIssueIds;
    }

    public String getInInsIssueIds() {
        return inInsIssueIds;
    }

    public void setInInsIssueIds(String inInsIssueIds) {
        this.inInsIssueIds = inInsIssueIds;
    }

    public Integer getNdeRatio() {
        return ndeRatio;
    }

    public void setNdeRatio(Integer ndeRatio) {
        this.ndeRatio = ndeRatio;
    }

    public String getMaterialGroupCode() {
        return materialGroupCode;
    }

    public void setMaterialGroupCode(String materialGroupCode) {
        this.materialGroupCode = materialGroupCode;
    }

    public InspectionFirstPassYield getInternalInspectionFpy() {
        return internalInspectionFpy;
    }

    public void setInternalInspectionFpy(InspectionFirstPassYield internalInspectionFpy) {
        this.internalInspectionFpy = internalInspectionFpy;
    }

    public InspectionFirstPassYield getExternalInspectionFpy() {
        return externalInspectionFpy;
    }

    public void setExternalInspectionFpy(InspectionFirstPassYield externalInspectionFpy) {
        this.externalInspectionFpy = externalInspectionFpy;
    }

    public Integer getWeldWelderCount() {
        return weldWelderCount;
    }

    public void setWeldWelderCount(Integer weldWelderCount) {
        this.weldWelderCount = weldWelderCount;
    }

    public boolean isExecuteNgFlag() {
        return executeNgFlag;
    }

    public void setExecuteNgFlag(boolean executeNgFlag) {
        this.executeNgFlag = executeNgFlag;
    }

    public Double getPlanHour() {
        return planHour;
    }

    public void setPlanHour(Double planHour) {
        this.planHour = planHour;
    }

    public Double getCostHour() {
        return costHour;
    }

    public void setCostHour(Double costHour) {
        this.costHour = costHour;
    }

    public String getCurrentExecutor() {
        return currentExecutor;
    }

    public void setCurrentExecutor(String currentExecutor) {
        this.currentExecutor = currentExecutor;
    }

    public String getCurrentTaskNode() {
        return currentTaskNode;
    }

    public void setCurrentTaskNode(String currentTaskNode) {
        this.currentTaskNode = currentTaskNode;
    }

    public Integer getPmiRatio() {
        return pmiRatio;
    }

    public void setPmiRatio(Integer pmiRatio) {
        this.pmiRatio = pmiRatio;
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

    public String getDrawingTitle() {
        return drawingTitle;
    }

    public void setDrawingTitle(String drawingTitle) {
        this.drawingTitle = drawingTitle;
    }

    public Long getWorkSiteId() {
        return workSiteId;
    }

    public void setWorkSiteId(Long workSiteId) {
        this.workSiteId = workSiteId;
    }

    public String getWorkSiteName() {
        return workSiteName;
    }

    public void setWorkSiteName(String workSiteName) {
        this.workSiteName = workSiteName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public int getUnAcceptCount() {
        return unAcceptCount;
    }

    public void setUnAcceptCount(int unAcceptCount) {
        this.unAcceptCount = unAcceptCount;
    }

    public void addUnAcceptCount() {
        this.unAcceptCount = unAcceptCount + 1;
    }

    public String getTaskPackageName() {
        return taskPackageName;
    }

    public void setTaskPackageName(String taskPackageName) {
        this.taskPackageName = taskPackageName;
    }

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    public Long getSpoolEntityId() {
        return spoolEntityId;
    }

    public void setSpoolEntityId(Long spoolEntityId) {
        this.spoolEntityId = spoolEntityId;
    }

    public String getSpoolNo() {
        return spoolNo;
    }

    public void setSpoolNo(String spoolNo) {
        this.spoolNo = spoolNo;
    }

    public Integer getWeldCount() {
        return weldCount;
    }

    public void setWeldCount(Integer weldCount) {
        this.weldCount = weldCount;
    }

    public Integer getWeldRepairCount() {
        return weldRepairCount;
    }

    public void setWeldRepairCount(Integer weldRepairCount) {
        this.weldRepairCount = weldRepairCount;
    }

    public Integer getWeldPenaltyCount() {
        return weldPenaltyCount;
    }

    public void setWeldPenaltyCount(Integer weldPenaltyCount) {
        this.weldPenaltyCount = weldPenaltyCount;
    }

    public String getWpsNo() {
        return wpsNo;
    }

    public void setWpsNo(String wpsNo) {
        this.wpsNo = wpsNo;
    }

    public Date getWeldCompleteTime() {
        return weldCompleteTime;
    }

    public void setWeldCompleteTime(Date weldCompleteTime) {
        this.weldCompleteTime = weldCompleteTime;
    }

    @JsonProperty(value = "inspectParties", access = JsonProperty.Access.READ_ONLY)
    public List<InspectParty> getJsonInspectParties() {
        if (inspectParties != null && !"".equals(inspectParties)) {
            return StringUtils.decode(inspectParties, new TypeReference<List<InspectParty>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonParties(List<InspectParty> inspectParties) {
        if (inspectParties != null) {
            this.inspectParties = StringUtils.toJSON(inspectParties);
        }
    }

    public String getInspectParties() {
        return inspectParties;
    }

    public void setInspectParties(String inspectParties) {
        this.inspectParties = inspectParties;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getWorkSiteAddress() {
        return workSiteAddress;
    }

    public void setWorkSiteAddress(String workSiteAddress) {
        this.workSiteAddress = workSiteAddress;
    }

    public Date getCurrentTaskNodeDate() {
        return currentTaskNodeDate;
    }

    public void setCurrentTaskNodeDate(Date currentTaskNodeDate) {
        this.currentTaskNodeDate = currentTaskNodeDate;
    }

    public String getIsNdtFinished() {
        return isNdtFinished;
    }

    public void setIsNdtFinished(String isNdtFinished) {
        this.isNdtFinished = isNdtFinished;
    }


}
