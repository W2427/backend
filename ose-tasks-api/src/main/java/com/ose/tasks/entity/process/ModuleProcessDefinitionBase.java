package com.ose.tasks.entity.process;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * 项目模块工作流定义。
 */
@MappedSuperclass
public class ModuleProcessDefinitionBase extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 5481712701451603855L;

    @Schema(description = "组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "bpmn名称")
    @Column(length = 64, nullable = false)
    private String bpmnName;

    @Schema(description = "定义文件 ID")
    @Column
    private Long fileId;

    @Schema(description = "流程图文件 ID")
    @Column
    private Long diagramFileId;

    @Schema(description = "Activiti 工作流部署 ID")
    @Column(length = 64, nullable = false)
    private String deploymentId;

    @Schema(description = "功能分块")
    @Column(length = 64, nullable = false)
    private String funcPart;

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

    public String getBpmnName() {
        return bpmnName;
    }

    public void setBpmnName(String bpmnName) {
        this.bpmnName = bpmnName;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getDiagramFileId() {
        return diagramFileId;
    }

    public void setDiagramFileId(Long diagramFileId) {
        this.diagramFileId = diagramFileId;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }
}
