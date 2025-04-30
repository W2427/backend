package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "drawing_annotation_reply")
public class DrawingAnnotationReply extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 1988913556908018886L;

    @Schema(description = "项目ID")
    @Column
    private Long projectId;

    @Schema(description = "组织ID")
    @Column
    private Long orgId;

    @Schema(description = "图纸ID")
    @Column
    private Long drawingId;

    @Schema(description = "图纸详情ID")
    @Column
    private Long drawingDetailId;

    @Schema(description = "图纸任务流程ID")
    @Column
    private Long procInstId;

    @Schema(description = "图纸评审意见表ID")
    @Column
    private Long drawingAnnotationId;

    @Schema(description = "图纸代办任务ID")
    @Column
    private Long taskId;

    @Schema(description = "图纸任务流程节点key")
    @Column
    private String taskDefKey;

    @Schema(description = "图纸任务流程节点名")
    @Column
    private String taskName;

    @Schema(description = "操作者")
    @Column
    private String user;

    @Schema(description = "回复建议")
    @Column
    private String comment;

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

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public Long getDrawingDetailId() {
        return drawingDetailId;
    }

    public void setDrawingDetailId(Long drawingDetailId) {
        this.drawingDetailId = drawingDetailId;
    }

    public Long getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(Long procInstId) {
        this.procInstId = procInstId;
    }

    public Long getDrawingAnnotationId() {
        return drawingAnnotationId;
    }

    public void setDrawingAnnotationId(Long drawingAnnotationId) {
        this.drawingAnnotationId = drawingAnnotationId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
