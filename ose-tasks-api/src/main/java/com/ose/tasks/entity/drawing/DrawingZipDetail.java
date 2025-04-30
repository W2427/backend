package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseBizEntity;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * 打包图纸历史记录详细
 */
@Entity
@Table(name = "drawing_zip_history")
public class DrawingZipDetail extends BaseBizEntity {

    private static final long serialVersionUID = -8507559632691397410L;

    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "组织id")
    private Long orgId;

    @Schema(description = "操作人名")
    private String operateName;

    @Schema(description = "操作人id")
    private Long operateBy;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "打包完成时间")
    private Date packageAt;

    @Schema(description = "文件id")
    private String fileId;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "图纸编号")
    private String DrawingNo;

    @Schema(description = "图纸版本")
    private String drawingVersion;

    @Schema(description = "是否redmark")
    @Enumerated(EnumType.STRING)
    private EntityStatus isRedMark;

    @Schema(description = "状态")
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    @Schema(description = "打包状态")
    private EntityStatus packageStatus = null;

    @Schema(description = "制图人")
    private String drawer;

    @Schema(description = "错误信息")
    private String errorMsg;


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

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public Long getOperateBy() {
        return operateBy;
    }

    public void setOperateBy(Long operateBy) {
        this.operateBy = operateBy;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getPackageAt() {
        return packageAt;
    }

    public void setPackageAt(Date packageAt) {
        this.packageAt = packageAt;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDrawingNo() {
        return DrawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        DrawingNo = drawingNo;
    }

    public EntityStatus getPackageStatus() {
        return packageStatus;
    }

    public void setPackageStatus(EntityStatus packageStatus) {
        this.packageStatus = packageStatus;
    }

    public String getDrawer() {
        return drawer;
    }

    public void setDrawer(String drawer) {
        this.drawer = drawer;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getDrawingVersion() {
        return drawingVersion;
    }

    public void setDrawingVersion(String drawingVersion) {
        this.drawingVersion = drawingVersion;
    }

    public EntityStatus getIsRedMark() {
        return isRedMark;
    }

    public void setIsRedMark(EntityStatus isRedMark) {
        this.isRedMark = isRedMark;
    }

    @Override
    public EntityStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(EntityStatus status) {
        this.status = status;
    }
}
