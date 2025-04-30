package com.ose.tasks.dto.drawing;

import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.vo.DrawingStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.Date;

/**
 * 生产设计图纸清单导入数据传输对象。
 */
public class DrawingCriteriaDTO extends WBSEntryCriteriaBaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "图纸类型id")
    private Long drawingCategoryId;

    @Schema(description = "任务")
    private Boolean actInst;

    @Schema(description = "状态")
    private Boolean locked;

    @Schema(description = "最新有效版本")
    private String latestApprovedRev;

    @Schema(description = "查询图纸类型")
    private Boolean bomFlag;

    @Schema(description = "图纸状态")
    @Enumerated(EnumType.STRING)
    private DrawingStatus drawingStatus;

    @Schema(description = "区域名称")
    private String areaName;

    @Schema(description = "模块名称")
    private String moduleName;

    @Schema(description = "信息整理")
    private String infoPut;

    @Schema(description = "工作网")
    private String workNet;

    @Schema(description = "分段")
    private String section;

    @Schema(description = "总段")
    private String block;

    @Schema(description = "小区域")
    private String smallArea;

    @Schema(description = "设计专业")
    private String designDiscipline;

    @Schema(description = "版本")
    private String latestRev;

    private String dwgNo;

    private String status;

    private String systemCode;

    private String discCode;

    private String docType;

    private String drawingType;

    private String sdrlCode;

    private String packageNo;

    private String originatorName;

    private String entityType;

    @Schema(description = "图纸设计人员id")
    private Long drawUserId;

    @Schema(description = "设计开始时间")
    private Date startTime;

    @Schema(description = "设计结束时间")
    private Date endTime;

    @Schema(description = "图纸校对人员id")
    private Long checkUserId;

    @Schema(description = "图纸审核人员id")
    private Long approvedUserId;

    @Schema(description = "任务人id")
    private Long assigneeId;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getDrawUserId() {
        return drawUserId;
    }

    public void setDrawUserId(Long drawUserId) {
        this.drawUserId = drawUserId;
    }

    public Long getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(Long checkUserId) {
        this.checkUserId = checkUserId;
    }

    public Long getApprovedUserId() {
        return approvedUserId;
    }

    public void setApprovedUserId(Long approvedUserId) {
        this.approvedUserId = approvedUserId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getDiscCode() {
        return discCode;
    }

    public void setDiscCode(String discCode) {
        this.discCode = discCode;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getSdrlCode() {
        return sdrlCode;
    }

    public void setSdrlCode(String sdrlCode) {
        this.sdrlCode = sdrlCode;
    }

    public String getPackageNo() {
        return packageNo;
    }

    public void setPackageNo(String packageNo) {
        this.packageNo = packageNo;
    }

    public String getOriginatorName() {
        return originatorName;
    }

    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public DrawingStatus getDrawingStatus() {
        return drawingStatus;
    }

    public void setDrawingStatus(DrawingStatus drawingStatus) {
        this.drawingStatus = drawingStatus;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getDrawingCategoryId() {
        return drawingCategoryId;
    }

    public void setDrawingCategoryId(Long drawingCategoryId) {
        this.drawingCategoryId = drawingCategoryId;
    }

    public Boolean getBomFlag() {
        return bomFlag;
    }

    public void setBomFlag(Boolean bomFlag) {
        this.bomFlag = bomFlag;
    }

    public Boolean getActInst() {
        return actInst;
    }

    public void setActInst(Boolean actInst) {
        this.actInst = actInst;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getLatestApprovedRev() {
        return latestApprovedRev;
    }

    public void setLatestApprovedRev(String latestApprovedRev) {
        this.latestApprovedRev = latestApprovedRev;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getInfoPut() {
        return infoPut;
    }

    public void setInfoPut(String infoPut) {
        this.infoPut = infoPut;
    }

    public String getWorkNet() {
        return workNet;
    }

    public void setWorkNet(String workNet) {
        this.workNet = workNet;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getSmallArea() {
        return smallArea;
    }

    public void setSmallArea(String smallArea) {
        this.smallArea = smallArea;
    }

    public String getDesignDiscipline() {
        return designDiscipline;
    }

    public void setDesignDiscipline(String designDiscipline) {
        this.designDiscipline = designDiscipline;
    }

    public String getLatestRev() {
        return latestRev;
    }

    public void setLatestRev(String latestRev) {
        this.latestRev = latestRev;
    }

    public String getDwgNo() {
        return dwgNo;
    }

    public void setDwgNo(String dwgNo) {
        this.dwgNo = dwgNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDrawingType() {
        return drawingType;
    }

    public void setDrawingType(String drawingType) {
        this.drawingType = drawingType;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }
}
