package com.ose.tasks.dto.qc;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.bpm.ExInspStatus;
import com.ose.tasks.vo.qc.NDEType;
import com.ose.tasks.vo.wbs.EntityTestResult;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public class TestResultDTO extends BaseDTO {


    private static final long serialVersionUID = -4166935721946904684L;

    @Schema(description = "orgId")
    private Long orgId;

    @Schema(description = "projectId")
    private Long projectId;

    @Schema(description = "工序阶段 processStage")
    private String processStage;

    @Schema(description = "工序 process")
    private String process;

    @Schema(description = "工序Id")
    private Long processId;

    @Schema(description = "外检结果")
    private ExInspStatus exInspStatus;

    @Schema(description = "检验员ID")
    private Long inspectorId;

    @Schema(description = "报告 二维码")
    private String reportQrCode;

    @Schema(description = "报告 编号")
    private String reportNo;

    @Schema(description = "操作员ID")
    private Long operatorId;

    @Schema(description = "是否通过测试")
    private EntityTestResult testResult;

    @Schema(description = "流程实例 ID")
    private Long actInstId;

    @Schema(description = "结果提交时间")
    private Date submittedAt;

    @Schema(description = "结果提交者（质检员）ID")
    private Long submittedBy;

    @Schema(description = "备注")
    private String comment;

    @Schema(description = "WPS Nos,逗号分隔")
    private String wpsNos;

    @Schema(description = "NDT TYPE")
    private NDEType ndtType;

    @Schema(description = "NDT RATIO")
    private Integer ndtRatio;

    @Schema(description = "work site Id")
    private Long workSiteId;

    @Schema(description = "work site")
    private String workSite;

    @Schema(description = "work team Id")
    private Long workTeamId;

    @Schema(description = "work team")
    private String workTeam;

    @Schema(description = "task package Id")
    private Long taskPackageId;

    @Schema(description = "task package")
    private String taskPackage;

    @Schema(description = "SUB ENTITY TYPE")
    private String entitySubType;

    @Schema(description = "完成于")
    private Date finishedAt;

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

    public ExInspStatus getExInspStatus() {
        return exInspStatus;
    }

    public void setExInspStatus(ExInspStatus exInspStatus) {
        this.exInspStatus = exInspStatus;
    }

    public Long getInspectorId() {
        return inspectorId;
    }

    public void setInspectorId(Long inspectorId) {
        this.inspectorId = inspectorId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public EntityTestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(EntityTestResult testResult) {
        this.testResult = testResult;
    }

    public String getReportQrCode() {
        return reportQrCode;
    }

    public void setReportQrCode(String reportQrCode) {
        this.reportQrCode = reportQrCode;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Long getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(Long submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

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

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getWpsNos() {
        return wpsNos;
    }

    public void setWpsNos(String wpsNos) {
        this.wpsNos = wpsNos;
    }

    public NDEType getNdtType() {
        return ndtType;
    }

    public void setNdtType(NDEType ndtType) {
        this.ndtType = ndtType;
    }

    public Integer getNdtRatio() {
        return ndtRatio;
    }

    public void setNdtRatio(Integer ndtRatio) {
        this.ndtRatio = ndtRatio;
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

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
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
