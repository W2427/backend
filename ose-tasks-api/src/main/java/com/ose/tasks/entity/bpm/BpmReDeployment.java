package com.ose.tasks.entity.bpm;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.dto.bpm.ActTaskNodeDTO;
import com.ose.tasks.vo.SuspensionState;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 流程部署实体类。
 */
@Entity
@Table(name = "bpm_re_deployment",
indexes = {
    @Index(columnList = "projectId, process_key")
})
public class BpmReDeployment extends BaseBizEntity {

    private static final long serialVersionUID = -5705592037581554410L;


    // 组织ID
    @Column
    private Long orgId;

    // 项目ID
    @Column
    private Long projectId;

    // 模板名称
    @Column(nullable = false, length = 128)
    @NotNull(message = "Process's name is required")
    private String processName;

    // 模板key process Id
    @Column(name = "process_key", length = 128)
    private Long processId;

    @Column(length = 128)
    @NotNull(message = "operator is required")
    private String operator;

    @Column(length = 128)
    @Schema(description = "act number 123456")
    private String actReDeploymentId;

    @Column(length = 511)
    private String filePath;

    @Column(length = 256)
    private Long fileId;

    @Column(length = 500)
    private String fileName;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private SuspensionState suspensionState;

    private int version;

    private Date deployTime;

    private String procDefId;

    private String category;

    @Transient
    private String diagramResource;

    @Transient
    private List<ActTaskNodeDTO> nodes;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
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

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getActReDeploymentId() {
        return actReDeploymentId;
    }

    public void setActReDeploymentId(String actReDeploymentId) {
        this.actReDeploymentId = actReDeploymentId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public SuspensionState getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(SuspensionState suspensionState) {
        this.suspensionState = suspensionState;
    }

    public void setSuspensionState(int suspensionState) {
        this.suspensionState = SuspensionState.ACTIVE;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(Date deployTime) {
        this.deployTime = deployTime;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    public String getDiagramResource() {
        return diagramResource;
    }

    public void setDiagramResource(String diagramResource) {
        this.diagramResource = diagramResource;
    }

    public String getEntitySubType() {
        return category;
    }

    public void setEntitySubType(String category) {
        this.category = category;
    }

    @Transient
    public List<ActTaskNodeDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<ActTaskNodeDTO> nodes) {
        this.nodes = nodes;
    }


}
