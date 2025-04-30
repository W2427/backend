package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.drawing.DrawingCoordinateType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class DrawingCommentDTO extends BaseDTO {

    private static final long serialVersionUID = 8547937155313823732L;
    @Schema(description = "实体类型 ID")
    private Long drawingId;

    @Schema(description = "图纸详情Id")
    private Long drawingDetailId;

    @Schema(description = "流程Id")
    private Long procInstId;

    @Schema(description = "待办任务Id")
    private Long ruTaskId;

    private String process;

    private String taskNode;

    private String content;

    private Long projectName;

    private String user;

    private Integer replies;

    public Long getRuTaskId() {
        return ruTaskId;
    }

    public void setRuTaskId(Long ruTaskId) {
        this.ruTaskId = ruTaskId;
    }

    public Long getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(Long procInstId) {
        this.procInstId = procInstId;
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

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getTaskNode() {
        return taskNode;
    }

    public void setTaskNode(String taskNode) {
        this.taskNode = taskNode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getProjectName() {
        return projectName;
    }

    public void setProjectName(Long projectName) {
        this.projectName = projectName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getReplies() {
        return replies;
    }

    public void setReplies(Integer replies) {
        this.replies = replies;
    }
}
