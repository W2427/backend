package com.ose.tasks.entity.drawing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.dto.BpmnSequenceNodeDTO;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;

import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "drawing_annotation")
public class DrawingAnnotation extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -4720296532084382143L;

    @Schema(description = "项目ID")
    @Column
    private Long projectId;

    private Integer pageNo;

    @Schema(description = "组织ID")
    @Column
    private Long orgId;

    @Schema(description = "图纸文件历史ID")
    @Column
    private Long drawingFileHistoryId;

    @Schema(description = "图纸详情ID")
    @Column
    private Long drawingDetailId;

    @Schema(description = "图纸任务流程ID")
    @Column
    private Long procInstId;

    @Schema(description = "图纸代办任务ID")
    @Column
    private Long taskId;

    @Column
    private Float pw;

    @Column
    private Float ph;

    @Column
    private Integer pageRotation;

    public Float getPw() {
        return pw;
    }

    public void setPw(Float pw) {
        this.pw = pw;
    }

    public Float getPh() {
        return ph;
    }

    public void setPh(Float ph) {
        this.ph = ph;
    }

    public Integer getPageRotation() {
        return pageRotation;
    }

    public void setPageRotation(Integer pageRotation) {
        this.pageRotation = pageRotation;
    }

    //    @Schema(description = "图纸任务流程节点key")
//    @Column
//    private String taskDefKey;
//
//    @Schema(description = "图纸任务流程节点名")
//    @Column
//    private String taskName;

    @Schema(description = "操作者")
    @Column
    private String user;

    @Schema(description = "评审意见")
    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String annotation;

    @Column
    private String annotationId;

    public String getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(String annotationId) {
        this.annotationId = annotationId;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }


    @JsonProperty(value = "annotation", access = JsonProperty.Access.READ_ONLY)
    public PDAnnotation getJsonAnnotation() {
        if (annotation != null && !"".equals(annotation)) {
            return StringUtils.decode(annotation, new TypeReference<PDAnnotation>() {
            });
        } else {
            return null;
        }
    }

    @JsonIgnore
    public void setJsonAnnotation(PDAnnotation annotation) {
        if (annotation != null) {
            this.annotation = StringUtils.toJSON(annotation);
        }
    }

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

    public Long getDrawingFileHistoryId() {
        return drawingFileHistoryId;
    }

    public void setDrawingFileHistoryId(Long drawingFileHistoryId) {
        this.drawingFileHistoryId = drawingFileHistoryId;
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
