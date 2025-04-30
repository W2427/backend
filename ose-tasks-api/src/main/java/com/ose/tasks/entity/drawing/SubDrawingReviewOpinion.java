package com.ose.tasks.entity.drawing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 子图纸校审意见
 */
@Entity
@Table(
    name = "sub_drawing_review_opinion",
    indexes = {
        @Index(columnList = "projectId,actInstId,subDrawingId,parentId")
    }
)
public class SubDrawingReviewOpinion extends BaseBizEntity {

    private Long orgId;

    private Long projectId;

    private Long actInstId;

    private Long drawingId;

    private Long subDrawingId;

    private Long operator;

    @Column(columnDefinition = "text")
    private String content;

    private Long parentId;

    @Transient
    private List<SubDrawingReviewOpinion> replyOpinions;

    @Transient
    private String operatorName;

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
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

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public Long getSubDrawingId() {
        return subDrawingId;
    }

    public void setSubDrawingId(Long subDrawingId) {
        this.subDrawingId = subDrawingId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    @JsonProperty(value = "operator", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getOperatorReference() {
        return this.operator == null ? null : new ReferenceData(this.operator);
    }

    @Override
    public Set<Long> relatedUserIDs() {
        Set<Long> relatedUserIDs = new HashSet<>();
        relatedUserIDs.add(this.operator);
        return relatedUserIDs;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<SubDrawingReviewOpinion> getReplyOpinions() {
        return replyOpinions;
    }

    public void setReplyOpinions(List<SubDrawingReviewOpinion> replyOpinions) {
        this.replyOpinions = replyOpinions;
    }
}
