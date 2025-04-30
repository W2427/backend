package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.vo.DrawingFileType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 图纸文件修改历史信息，DrawingFile 通过 id 关联 DrawingFileHistory, 一对多关系
 * DrawingFileHistory中的文件status 分为 ACTIVE，DELETED
 * 字段 fileType 分为 ISSUE_FILE，OWNER_COMMENT，OWNER_COMMENT_REPLY, CLASS_COMMENT, CLASS_COMMENT_REPLY, OTHER
 */
@Entity
@Table(name = "drawing_file_history")
public class DrawingFileHistory extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 2519246537487507093L;

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

    @Schema(description = "图纸文件ID")
    @Column
    private Long drawingFileId;

    @Schema(description = "图纸任务流程ID")
    @Column
    private Long procInstId;

    @Schema(description = "图纸代办任务ID")
    @Column
    private Long taskId;

    @Schema(description = "图纸任务流程节点key")
    @Column
    private String taskDefKey;

    @Schema(description = "图纸任务流程节点名")
    @Column
    private String taskName;

    @Schema(description = "图纸文件ID")
    @Column
    private Long fileId;

    @Schema(description = "图纸文件名")
    @Column
    private String fileName;

    @Schema(description = "图纸文件存放路径")
    @Column
    private String filePath;

    @Schema(description = "PDF图纸文件数量")
    @Column
    private int pageCount = 0;

    @Schema(description = "图纸文件类型")
    @Column
    @Enumerated(EnumType.STRING)
    private DrawingFileType drawingFileType;

//    @Schema(description = "审批行为")
//    @Lob
//    @Column(length = 4096)
//    private String annotations;

    @Schema(description = "图纸版本")
    @Column
    private String rev;

    @Schema(description = "上传人")
    @Column
    private String user;

//    @Transient
//    private Object annotationObjects;

    @Schema(description = "是否是最新历史")
    @Column
    private Boolean isLatest = false;

    public Long getDrawingDetailId() {
        return drawingDetailId;
    }

    public void setDrawingDetailId(Long drawingDetailId) {
        this.drawingDetailId = drawingDetailId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public DrawingFileType getDrawingFileType() {
        return drawingFileType;
    }

    public void setDrawingFileType(DrawingFileType drawingFileType) {
        this.drawingFileType = drawingFileType;
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

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public Long getDrawingFileId() {
        return drawingFileId;
    }

    public void setDrawingFileId(Long drawingFileId) {
        this.drawingFileId = drawingFileId;
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

//    public String getAnnotations() {
//        return annotations;
//    }
//
//    public void setAnnotations(String annotations) {
//        this.annotations = annotations;
//    }
//
//    public Object getAnnotationObjects() {
//        return annotationObjects;
//    }
//
//    public void setAnnotationObjects(Object annotationObjects) {
//        this.annotationObjects = annotationObjects;
//    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Boolean getLatest() {
        return isLatest;
    }

    public void setLatest(Boolean latest) {
        isLatest = latest;
    }
}
