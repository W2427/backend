package com.ose.tasks.entity.drawing;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;

/**
 * 子图纸zip文件上传历史
 */
@Entity
@Table(name = "drawing_upload_zip_file_history")
public class DrawingUploadZipFileHistory extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -7837891255273680991L;

    private Long orgId;

    private Long projectId;

    //图纸条目id
    private Long drawingId;

    //页数
    private int fileCount;

    //成功检数
    private int successCount;

    //成功检数
    private int failedCount;

    //文件Id
    private Long fileId;

    //文件名
    private String fileName;

    //文件路径
    private String filePath;

    //上传者
    private Long operator;

    private boolean zipFile = true;

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

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
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

    /**
     * 取得操作人引用信息。
     *
     * @return 操作人引用信息
     */
    @JsonProperty(value = "operator", access = READ_ONLY)
    public ReferenceData getOperatorRef() {
        return this.operator == null
            ? null
            : new ReferenceData(this.operator);
    }

    @Override
    public Set<Long> relatedUserIDs() {

        Set<Long> userIDs = new HashSet<>();

        if (this.getOperator() != null && this.getOperator() != 0L) {
            userIDs.add(this.getOperator());
        }

        return userIDs;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public boolean isZipFile() {
        return zipFile;
    }

    public void setZipFile(boolean zipFile) {
        this.zipFile = zipFile;
    }

}
