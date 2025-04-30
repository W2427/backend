package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 拆分PDF历史实体。split
 *
 * @auth DengMing
 * @date 2021/8/3 11:21
 */
@Entity
@Table(
    name = "split_pdf_history",
    indexes = {
        @Index(columnList = "orgId,projectId")
    }
)
public class SplitPDFHistory extends BaseBizEntity {

    private static final long serialVersionUID = 8816852729317138499L;

    @Schema(description = "组织ID")
    private Long orgId;

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "图纸编号")
    private String drawingNo;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "操作者姓名")
    private String operatorName;

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "错误信息")
    private String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
