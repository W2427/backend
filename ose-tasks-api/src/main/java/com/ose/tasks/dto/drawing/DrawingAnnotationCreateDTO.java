package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 图纸评审意见创建传输对象。
 */
public class DrawingAnnotationCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 12185767959635690L;

    @Schema(description = "图纸ID")
    private Long drawingId;

    @Schema(description = "图纸详情ID")
    private Long drawingDetailId;

    @Schema(description = "图纸任务流程ID")
    private Long procInstId;

    @Schema(description = "图纸代办任务ID")
    private Long taskId;

    @Schema(description = "图纸任务流程节点key")
    private String taskDefKey;

    @Schema(description = "图纸任务流程节点名")
    private String taskName;

    @Schema(description = "操作者")
    private String user;

    @Schema(description = "评审意见")
    private String comment;

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
