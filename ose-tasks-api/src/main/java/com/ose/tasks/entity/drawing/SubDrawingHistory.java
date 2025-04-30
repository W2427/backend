package com.ose.tasks.entity.drawing;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 子图纸
 */
@Entity
@Table(name = "sub_drawing_history")
public class SubDrawingHistory extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -7837891255273680991L;


    private Long orgId;

    private Long projectId;

    private String qrCode;

    //子图纸条目id
    private Long subDrawingId;

    @Schema(description = "图纸详情id")
    private Long drawingDetailId;

    //文件id
    private Long fileId;

    //文件名
    private String fileName;

    //文件路径
    private String filePath;

    //文件页数
    private int filePageCount;

    //备注
    private String memo;


    private Long operator;

    //图纸版本
    private String subDrawingVersion;

    //图纸编号
    private String subDrawingNo;

    //页码信息
    private int pageNo;

    //是否被使用
    private boolean used = false;

    private boolean issueFlag = false;

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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getSubDrawingId() {
        return subDrawingId;
    }

    public void setSubDrawingId(Long subDrawingId) {
        this.subDrawingId = subDrawingId;
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

    public void setOperator(Long operatorId) {
        this.operator = operatorId;
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

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getSubDrawingVersion() {
        return subDrawingVersion;
    }

    public void setSubDrawingVersion(String subDrawingVersion) {
        this.subDrawingVersion = subDrawingVersion;
    }

    public String getSubDrawingNo() {
        return subDrawingNo;
    }

    public void setSubDrawingNo(String subDrawingNo) {
        this.subDrawingNo = subDrawingNo;
    }

    public int getFilePageCount() {
        return filePageCount;
    }

    public void setFilePageCount(int filePageCount) {
        this.filePageCount = filePageCount;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isIssueFlag() {
        return issueFlag;
    }

    public void setIssueFlag(boolean issueFlag) {
        this.issueFlag = issueFlag;
    }

    public Long getDrawingDetailId() {
        return drawingDetailId;
    }

    public void setDrawingDetailId(Long drawingDetailId) {
        this.drawingDetailId = drawingDetailId;
    }
}
