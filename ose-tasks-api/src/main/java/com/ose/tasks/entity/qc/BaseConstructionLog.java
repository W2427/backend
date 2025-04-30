package com.ose.tasks.entity.qc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.vo.wbs.EntityTestResult;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 质量控制实施记录基类。
 */
@MappedSuperclass
public abstract class BaseConstructionLog extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 6831973870818599525L;

    @Schema(description = "组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "实体 ID")
    @Column(nullable = false)
    private Long entityId;

    @Schema(description = "实体类型")
    @Column(nullable = false, length = 32)

    private String entityType;

    @Schema(description = "实体子类型")
    @Column(nullable = false, length = 64)
    private String entitySubType;

    @Schema(description = "工序阶段 processStage")
    @Column
    private String processStage;

    @Schema(description = "工序 process")
    @Column
    private String process;

    @Schema(description = "工序Id")
    @Column
    private Long processId;

    @Schema(description = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Column
    private Date createdAt;

    @Schema(description = "创建者 ID")
    @Column
    private Long createdBy;

    @Schema(description = "是否通过测试")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private EntityTestResult testResult;

    @Schema(description = "测试结果备注")
    @Column(columnDefinition = "TEXT")
    private String comment;

    @Schema(description = "报告文件 QR-CODEs, report Qr codes ")
    @Column
    private String reportQrCodes;

    @Schema(description = "报告编号 nos")
    @Column(length = 255)
    private String reportNos;

    @Schema(description = "报告实体文件 IDs")
    @Column
    private String fileIds;

    @Schema(description = "焊工，工人 Nos,逗号分隔")
    @Column(length = 511)
    private String executors;

    @Schema(description = "工人执行时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Column
    private Date performedAt;

    @Schema(description = "内检 QC")
    @Column
    private String internalQc;

    @Schema(description = "内检执行时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Column
    private Date internalQcPerformedAt;

    @Schema(description = "主管")
    @Column
    private String supervisor;

    @Schema(description = "主管执行时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Column
    private Date supervisorPerformedAt;

    @Schema(description = "QC")
    @Column
    private String qc;

    @Schema(description = "QC执行时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Column
    private Date qcPerformedAt;

    @Schema(description = "工作流ID, 123456")
    @Column(length = 20)
    private Long actInstId;

    @Schema(description = "work site Id")
    @Column
    private Long workSiteId;

    @Schema(description = "work site")
    @Column
    private String workSite;

    @Schema(description = "work team Id")
    @Column
    private Long workTeamId;

    @Schema(description = "work team")
    @Column
    private String workTeam;

    @Schema(description = "task package Id")
    @Column
    private Long taskPackageId;

    @Schema(description = "Task Package")
    @Column
    private String taskPackage;

    @Schema(description = "完成时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Column
    private Date finishedAt;


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

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty(value = "createdBy", access = READ_ONLY)
    public ReferenceData getCreatedByRef() {
        return createdBy == null ? null : new ReferenceData(createdBy);
    }

    public EntityTestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(EntityTestResult testResult) {
        this.testResult = testResult;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getProcessStage() {
        return processStage;
    }

    public void setProcessStage(String processStage) {
        this.processStage = processStage;
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

    public String getReportQrCodes() {
        return reportQrCodes;
    }

    public void setReportQrCodes(String reportQrCodes) {
        this.reportQrCodes = reportQrCodes;
    }

    public String getReportNos() {
        return reportNos;
    }

    public void setReportNos(String reportNos) {
        this.reportNos = reportNos;
    }

    public String getFileIds() {
        return fileIds;
    }

    public void setFileIds(String fileIds) {
        this.fileIds = fileIds;
    }

    public Long getWorkSiteId() {
        return workSiteId;
    }

    public void setWorkSiteId(Long workSiteId) {
        this.workSiteId = workSiteId;
    }

    public String getWorkSite() {
        return workSite;
    }

    public void setWorkSite(String workSite) {
        this.workSite = workSite;
    }

    public Long getWorkTeamId() {
        return workTeamId;
    }

    public void setWorkTeamId(Long workTeamId) {
        this.workTeamId = workTeamId;
    }

    public String getWorkTeam() {
        return workTeam;
    }

    public void setWorkTeam(String workTeam) {
        this.workTeam = workTeam;
    }

    public Date getPerformedAt() {
        return performedAt;
    }

    public void setPerformedAt(Date performedAt) {
        this.performedAt = performedAt;
    }

    public String getExecutors() {
        return executors;
    }

    public void setExecutors(String executors) {
        this.executors = executors;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public Date getSupervisorPerformedAt() {
        return supervisorPerformedAt;
    }

    public void setSupervisorPerformedAt(Date supervisorPerformedAt) {
        this.supervisorPerformedAt = supervisorPerformedAt;
    }

    public String getQc() {
        return qc;
    }

    public void setQc(String qc) {
        this.qc = qc;
    }

    public Date getQcPerformedAt() {
        return qcPerformedAt;
    }

    public void setQcPerformedAt(Date qcPerformedAt) {
        this.qcPerformedAt = qcPerformedAt;
    }

    public String getInternalQc() {
        return internalQc;
    }

    public void setInternalQc(String internalQc) {
        this.internalQc = internalQc;
    }

    public Date getInternalQcPerformedAt() {
        return internalQcPerformedAt;
    }

    public void setInternalQcPerformedAt(Date internalQcPerformedAt) {
        this.internalQcPerformedAt = internalQcPerformedAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    public String getTaskPackage() {
        return taskPackage;
    }

    public void setTaskPackage(String taskPackage) {
        this.taskPackage = taskPackage;
    }
}
