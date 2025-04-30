package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.vo.drawing.DrawingCoordinateType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;

import jakarta.persistence.*;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "drawing_comment")
public class DrawingComment extends BaseBizEntity {

    private static final long serialVersionUID = 4023969544032858063L;
    @Schema(description = "组织 ID")
    private Long orgId;

    @Schema(description = "项目 ID")
    private Long projectId;

    @Schema(description = "项目")
    private String projectName;

    @Id
    @ManyToOne
    @JoinColumn(name = "drawing_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private Drawing drawing;

    @Id
    @ManyToOne
    @JoinColumn(name = "drawing_detail_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private DrawingDetail drawingDetail;

    private String process;

    private String taskNode;

    @Column(length = 102400)
    private String content;

    private String designer;

    private Long designerId;

    private Long ruTaskId;

    private String user;

    private Long userId;

    private Integer replies;

    public Long getRuTaskId() {
        return ruTaskId;
    }

    public void setRuTaskId(Long ruTaskId) {
        this.ruTaskId = ruTaskId;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public Long getDesignerId() {
        return designerId;
    }

    public void setDesignerId(Long designerId) {
        this.designerId = designerId;
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

    public Drawing getDrawing() {
        return drawing;
    }

    public void setDrawing(Drawing drawing) {
        this.drawing = drawing;
    }

    public DrawingDetail getDrawingDetail() {
        return drawingDetail;
    }

    public void setDrawingDetail(DrawingDetail drawingDetail) {
        this.drawingDetail = drawingDetail;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getReplies() {
        return replies;
    }

    public void setReplies(Integer replies) {
        this.replies = replies;
    }
}
