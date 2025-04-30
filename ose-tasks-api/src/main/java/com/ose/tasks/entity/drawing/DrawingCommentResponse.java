package com.ose.tasks.entity.drawing;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.Id;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "drawing_comment_response")
public class DrawingCommentResponse extends BaseBizEntity {

    private static final long serialVersionUID = -1703803144221836788L;

    @Schema(description = "组织 ID")
    private Long orgId;

    @Schema(description = "项目 ID")
    private Long projectId;

    @Schema(description = "项目")
    private String projectName;

    @Id
    @ManyToOne
    @JoinColumn(name = "drawing_comment_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private DrawingComment drawingComment;

    @JsonManagedReference
    @OneToMany(mappedBy = "parentResponse")
    @Where(clause = "status != 'DELETED'")
    private List<DrawingCommentResponse> drawingCommentResponses ;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "parent_response_id")
    private DrawingCommentResponse parentResponse;

    @Column(length = 102400)
    private String content;

    private String user;

    private Long userId;

    private Long ruTaskId;

    private Integer replies;

    public Long getRuTaskId() {
        return ruTaskId;
    }

    public void setRuTaskId(Long ruTaskId) {
        this.ruTaskId = ruTaskId;
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


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DrawingComment getDrawingComment() {
        return drawingComment;
    }

    public void setDrawingComment(DrawingComment drawingComment) {
        this.drawingComment = drawingComment;
    }

    public List<DrawingCommentResponse> getDrawingCommentResponses() {
        return drawingCommentResponses;
    }

    public void setDrawingCommentResponses(List<DrawingCommentResponse> drawingCommentResponses) {
        this.drawingCommentResponses = drawingCommentResponses;
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

    public DrawingCommentResponse getParentResponse() {
        return parentResponse;
    }

    public void setParentResponse(DrawingCommentResponse parentResponse) {
        this.parentResponse = parentResponse;
    }
}
