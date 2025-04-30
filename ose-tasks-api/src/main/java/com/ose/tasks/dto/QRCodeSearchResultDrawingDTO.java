package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * 二维码。
 */
public class QRCodeSearchResultDrawingDTO extends BaseDTO {

    private static final long serialVersionUID = 3306151730639645725L;

    @Schema(description = "组织ID")
    private Long orgId;

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "文件Id")
    private Long fileId;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "图纸编号")
    private String drawingNo;

    @Schema(description = "图纸简码")
    private String shortCode;

    @Schema(description = "图纸名称")
    private String drawingTitle;

    @Schema(description = "图集当前版本")
    private String rev;

    @Schema(description = "图集最新版本")
    private String latestRev;

    @Schema(description = "子图纸编号")
    private String subDrawingNo;

    @Schema(description = "子图纸预览base64")
    private String subDrawingBase;

    @Schema(description = "当前子图纸版本")
    private String subRev;

    @Schema(description = "子图纸最新版本")
    private String subLastRev;

    @Schema(description = "页码")
    private Integer pageNo;

    @Schema(description = "页数")
    private Integer pageCount;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "是否redmark")
    private Boolean redmarkFlag;

    @Schema(description = "设计者")
    private String operator;

    @Schema(description = "校对人员")
    private String checkUser;

    @Schema(description = "审核人员")
    private String approveUser;

    @Schema(description = "二维码类型")
    private String qrCodeType;

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getDrawingTitle() {
        return drawingTitle;
    }

    public void setDrawingTitle(String drawingTitle) {
        this.drawingTitle = drawingTitle;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getSubDrawingNo() {
        return subDrawingNo;
    }

    public void setSubDrawingNo(String subDrawingNo) {
        this.subDrawingNo = subDrawingNo;
    }

    public String getSubRev() {
        return subRev;
    }

    public void setSubRev(String subRev) {
        this.subRev = subRev;
    }

    public String getSubLastRev() {
        return subLastRev;
    }

    public void setSubLastRev(String subLastRev) {
        this.subLastRev = subLastRev;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Boolean getRedmarkFlag() {
        return redmarkFlag;
    }

    public void setRedmarkFlag(Boolean redmarkFlag) {
        this.redmarkFlag = redmarkFlag;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public String getLatestRev() {
        return latestRev;
    }

    public void setLatestRev(String latestRev) {
        this.latestRev = latestRev;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(String qrCodeType) {
        this.qrCodeType = qrCodeType;
    }

    public String getSubDrawingBase() {
        return subDrawingBase;
    }

    public void setSubDrawingBase(String subDrawingBase) {
        this.subDrawingBase = subDrawingBase;
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
}
