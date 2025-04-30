package com.ose.tasks.dto.bpm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.dto.BaseDTO;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.qc.ReportSubTypeDTO;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.InspectParty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BpmActivityInstanceDTO extends BaseDTO {


    private static final long serialVersionUID = -7843580847506868713L;

    public BpmActivityInstanceDTO() {

    }

    @JsonCreator
    public BpmActivityInstanceDTO(@JsonProperty("reportSubTypeInfo") List<ReportSubTypeDTO> reportSubTypeInfo,
                                  @JsonProperty("reports") List<ActReportDTO> reports,
                                  @JsonProperty("inspectParties") List<InspectParty> inspectParties) {

        this.reports = StringUtils.toJSON(reports);
        this.reportSubTypeInfo = StringUtils.toJSON(reportSubTypeInfo);
        this.inspectParties = StringUtils.toJSON(inspectParties);

    }

    private Long id;

    @Schema(description = "创建时间")
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date createdAt;

    @Schema(description = "最后更新时间")
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date lastModifiedAt;

    @Schema(description = "数据实体状态")
    private EntityStatus status;

    //项目id
    private Long projectId;

    //组织id
    private Long orgId;

    @Schema(description = "版本")
    private String version;

    @Schema(description = "备注")
    private String memo;

    @Schema(description = "实体编号")
    private String entityNo;

    @Schema(description = "实体编号1")
    private String entityNo1;

    @Schema(description = "实体编号2")
    private String entityNo2;

    private String drawingTitle;

    private String discipline;

    private Long entityId;

    private String taskDefKey;

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

    @Schema(description = "流程担当者id")
    private Long ownerId;

    @Schema(description = "流程担当者名")
    private String ownerName;

    @Schema(description = "流程分配者id")
    private Long allocatee;

    @Schema(description = "分配时间")
    private Date allocateeDate;

    @Schema(description = "计划开始时间")
    private Date planStartDate;

    @Schema(description = "计划结束时间")
    private Date planEndDate;

    @Schema(description = "计划工时")
    private Double planHour;

    @Schema(description = "实际开始时间")
    private Date startDate;

    @Schema(description = "实际结束时间")
    private Date endDate;

    @Schema(description = "工时")
    private Double costHour;

    @Schema(description = "挂起状态")
    private SuspensionState suspensionState;

    @Schema(description = "完成状态")
    private ActInstFinishState finishState;

    @Schema(description = "流程类型")
    private String actCategory;

    @Schema(description = "工序分类id")
    private Long processCategoryId;

    @Schema(description = "报告内容")
    private String reports;

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

    @Schema(description = "流程内驳回次数")
    private int unAcceptCount = 0;

    @Schema(description = "检验方")
    private String inspectParties;

    @Schema(description = "当前任务节点")
    private String currentTaskNode;

    @Schema(description = "当前任务节点")
    private Date currentTaskNodeDate;

    @Schema(description = "子报告 主报告关系")
    private String reportSubTypeInfo;

    @Schema(description = "计划开始时间")
    private Date taskPackagePlanStartDate;

    @Schema(description = "计划结束时间")
    private Date taskPackagePlanEndDate;

    private String functionNo;

    private String functionDescription;

    private String typeNo;

    private String typeDescription;

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getFunctionNo() {
        return functionNo;
    }

    public void setFunctionNo(String functionNo) {
        this.functionNo = functionNo;
    }

    public String getFunctionDescription() {
        return functionDescription;
    }

    public void setFunctionDescription(String functionDescription) {
        this.functionDescription = functionDescription;
    }

    public String getTypeNo() {
        return typeNo;
    }

    public void setTypeNo(String typeNo) {
        this.typeNo = typeNo;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
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
