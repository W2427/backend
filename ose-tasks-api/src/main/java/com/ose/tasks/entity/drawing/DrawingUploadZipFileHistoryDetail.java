package com.ose.tasks.entity.drawing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.drawing.DrawingUploadZipFileSuccessFlg;

/**
 * 子图纸zip文件上传历史记录详细
 */
@Entity
@Table(name = "drawing_upload_zip_file_history_detail")
public class DrawingUploadZipFileHistoryDetail extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -7837891255273680991L;

    private Long orgId;

    private Long projectId;

    private Long drawingUploadZipFileHistoryId;

    //成功检数
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private DrawingUploadZipFileSuccessFlg successFlg;

    //文件名
    private String fileName;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public DrawingUploadZipFileSuccessFlg getSuccessFlg() {
        return successFlg;
    }

    public void setSuccessFlg(DrawingUploadZipFileSuccessFlg successFlg) {
        this.successFlg = successFlg;
    }

    public Long getDrawingUploadZipFileHistoryId() {
        return drawingUploadZipFileHistoryId;
    }

    public void setDrawingUploadZipFileHistoryId(Long drawingUploadZipFileHistoryId) {
        this.drawingUploadZipFileHistoryId = drawingUploadZipFileHistoryId;
    }

}
