package com.ose.tasks.entity.drawing.externalDrawing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 输入图纸历史表
 */
@Entity
@Table(name = "drawing_amendment_history")
public class DrawingAmendmentHistory extends BaseBizEntity {


    private static final long serialVersionUID = 4309862805633078709L;
    @Schema(description = "ORG ID 组织ID")
    @Column
    private Long orgId;

    @Schema(description = "PROJECT ID 项目ID")
    @Column
    private Long projectId;

    @Schema(description = "图纸二维码")
    @Column
    private String qrCode;

    //图纸条目id
    @Schema(description = "图纸ID")
    @Column
    private Long drawingId;

    //文件id
    @Schema(description = "文件ID")
    @Column
    private Long fileId;

    //文件名
    @Schema(description = "文件名")
    @Column
    private String fileName;

    //
    @Schema(description = "文件路径")
    @Column
    private String filePath;

    //文件页数
    @Schema(description = "ORG ID 组织ID")
    @Column
    private String filePageCount;

    //
    @Schema(description = "备注")
    @Column
    private String memo;

    //
    @Schema(description = "版本号")
    @Column
    private String verison;

    @Schema(description = "操作者ID")
    @Column
    private Long operator;

    @Schema(description = "是否发图标记")
    @Column
    private boolean issueFlag = false;

    public DrawingAmendmentHistory(ExternalDrawing drawing){
        this.setCreatedAt();
        this.drawingId = (drawing.getId());
        this.setOrgId(drawing.getOrgId());
        this.setProjectId(drawing.getProjectId());
        this.setStatus(EntityStatus.ACTIVE);
    }

    public DrawingAmendmentHistory(){

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

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
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

    public String getFilePageCount() {
        return filePageCount;
    }

    public void setFilePageCount(String filePageCount) {
        this.filePageCount = filePageCount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getVerison() {
        return verison;
    }

    public void setVerison(String verison) {
        this.verison = verison;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
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

    public boolean isIssueFlag() {
        return issueFlag;
    }

    public void setIssueFlag(boolean issueFlag) {
        this.issueFlag = issueFlag;
    }


}
