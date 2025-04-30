package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseBizEntity;
import com.ose.vo.DrawingFileType;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 图纸 相关的 文件信息， DrawingDetail 通过 id 关联 DrawingFile, 一对多关系
 * DrawingFile中的文件status 分为 ACTIVE，DELETED
 * 字段 fileType 分为 ISSUE_FILE，OWNER_COMMENT，OWNER_COMMENT_REPLY, CLASS_COMMENT, CLASS_COMMENT_REPLY, OTHER
 */
@Entity
@Table(name = "drawing_file")
public class DrawingFile extends BaseBizEntity {

    private static final long serialVersionUID = -241569299834906872L;

    private Long projectId;

    private Long orgId;

    private Long drawingDetailId;

    private Long fileId;

    private String fileName;

    private String filePath;

    private Long uploaderId;

    private String uploaderName;

    private Date uploadDate;

    private int pageCount = 0;


    @Enumerated(EnumType.STRING)
    private DrawingFileType drawingFileType;

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

    public Long getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(Long uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
