package com.ose.tasks.entity.constructionLog;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.report.vo.log.EntityTestResult;
import com.ose.tasks.vo.ConstStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 质量控制实施记录类。
 */
@Entity
@Table(name = "construction_log",
    indexes = {
        @Index(columnList = "entityId,actInstId,deleted", unique = true),
        @Index(columnList = "actInstId,testResult,deleted"),
        @Index(columnList = "projectId,wp01No,entityType,inspectionFinishedStatus,inspectionFinishedDate"),
        @Index(columnList = "orgId,projectId,id,deleted"),
        @Index(columnList = "processId")
    })
public class ConstructionLog extends BaseVersionedBizEntity {


    private static final long serialVersionUID = 7547447706413340216L;
    @Schema(description = "组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "实体 ID")
    @Column(nullable = false)
    private Long entityId;

    @Schema(description = "实体 编号")
    @Column(nullable = false)
    private String entityNo;

    @Schema(description = "实体类型")
    @Column(nullable = false)
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

    @Schema(description = "建造状态")
    @Column
    @Enumerated(EnumType.STRING)
    private ConstStatus constStatus;

    @Schema(description = "建造状态")
    @Column
    private Integer constStatusIndex;

    @Schema(description = "测试结果备注")
    @Column(columnDefinition = "text")
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
    @Column(length = 16)
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

    @Schema(description = "总段号")
    @Column
    private String wp01No;

    @Schema(description = "重量")
    @Column
    private Double weight = 0.0;

    @Schema(description = "长度")
    @Column
    private Double length = 0.0;

    @Schema(description = "内检完成时间")
    @Column
    private Date inspectionFinishedDate;

    @Schema(description = "内检完成状态")
    @Column
    private Boolean inspectionFinishedStatus =false;

    @Schema(description = "父级ID")
    @Column
    private Long parentEntityId;

    @Schema(description = "父级No")
    @Column
    private String parentEntityNo;

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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Date getInspectionFinishedDate() {
        return inspectionFinishedDate;
    }

    public void setInspectionFinishedDate(Date inspectionFinishedDate) {
        this.inspectionFinishedDate = inspectionFinishedDate;
    }

    public Boolean getInspectionFinishedStatus() {
        return inspectionFinishedStatus;
    }

    public void setInspectionFinishedStatus(Boolean inspectionFinishedStatus) {
        this.inspectionFinishedStatus = inspectionFinishedStatus;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getWp01No() {
        return wp01No;
    }

    public void setWp01No(String wp01No) {
        this.wp01No = wp01No;
    }

    public Long getParentEntityId() {
        return parentEntityId;
    }

    public void setParentEntityId(Long parentEntityId) {
        this.parentEntityId = parentEntityId;
    }

    public String getParentEntityNo() {
        return parentEntityNo;
    }

    public void setParentEntityNo(String parentEntityNo) {
        this.parentEntityNo = parentEntityNo;
    }

    public ConstStatus getConstStatus() {
        return constStatus;
    }

    public void setConstStatus(ConstStatus constStatus) {
        this.constStatus = constStatus;
    }

    public Integer getConstStatusIndex() {
        return constStatusIndex;
    }

    public void setConstStatusIndex(Integer constStatusIndex) {
        this.constStatusIndex = constStatusIndex;
    }
}
