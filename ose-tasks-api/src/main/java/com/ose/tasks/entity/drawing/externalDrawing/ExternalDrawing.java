package com.ose.tasks.entity.drawing.externalDrawing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 输入图集
 */
@Entity
@Table(name = "external_drawing")
public class ExternalDrawing extends BaseBizEntity {

    private static final long serialVersionUID = -4253400134732785021L;
    /**
     *
     */
    @Schema(description = "是否有正在运行的图纸流程")
    @Transient
    private boolean actInst = false;

    private Long orgId;

    private Long projectId;

    private Long importFileId;

    private Long batchTaskId;

    @Column(length = 9)
    private String qrCode;

    //序号
    private String sequenceNo;

    //图号
    private String dwgNo;

    //文件名称
    private String drawingTitle;

    //预估工时
    @Column(columnDefinition = "decimal(9,1) default 0.0")
    private Double estimatedManHours;

    private Date engineeringFinishDate;

    private Date engineeringStartDate;

    //工作版本版本
    private String latestRev;

    //最新有效版本
    private String latestApprovedRev;

    //计划提交建造时间
    private Date deliveryDate;

    //实际发图日期
    private Date acturalDrawingIssueDate;

    //生产接收日期
    private Date productionReceivingDate;

    //发放记录卡编号
    private String issueCardNo;

    //回收记录
    private String returnRecord;

    //设计评审变更单号
    private String designChangeReviewForm;

    //变更通知单号
    private String changeNoticeNo;

    //配布份数
    private Integer quantity = 0;

    //打印份数 = 配布份数-1
    private Integer printing = 0;

    //用纸量（A3）
    private Float paperUse = (float) 0;

    //纸张数量 = 打印份数*用纸量
    private Float paperAmount = (float) 0;

    //图纸审核编号
    private String auditNo;

    @Schema(description = "是否 有 RED-MARK 流程在运行")
    @Column
    private Boolean isRedMarkOnGoing;

    //图纸类型
    @Column
    private String entitySubType;

    //图纸文件id
    private Long fileId;

    //图纸文件名称
    private String fileName;

    //图纸文件路径
    private String filePath;

    //图纸文件最新版本
    private int fileLastVersion;

    //图纸文件页数
    private String filePageCount;

    private Long operator;

    //图纸是否被锁定，只有在设计环节不是锁定的，false
    private boolean locked = false;

    //图纸封面文件id
    private Long coverId;

    //图纸封面文件Name
    private String coverName;

    //图纸封面文件路径
    private String coverPath;

    @Transient
    private String diagramResource;

    //制图人
    private String drawer;

    //制图人id
    private Long drawerId;

    private String memo;

    private Boolean isInfoPut;

    @Schema(description = "设计人员用户名")
    private String drawUsername;

    @Schema(description = "校对人员用户名")
    private String checkUsername;

    @Schema(description = "审核人员用户名")
    private String approvedUsername;

    @Schema(description = "设计人员")
    @Transient
    private Long drawUserId;

    @Schema(description = "校对人员")
    @Transient
    private Long checkUserId;

    @Schema(description = "审核人员")
    @Transient
    private Long approvedUserId;

    @Schema(description = "待提交图纸数量")
    @Transient
    private Integer initCount;

    @Schema(description = "校对中图纸数量")
    @Transient
    private Integer checkCount;

    @Schema(description = "审核中图纸数量")
    @Transient
    private Integer reviewCount;

    @Schema(description = "修改中图纸数量")
    @Transient
    private Integer modifyCount;

    @Schema(description = "已校对图纸数量")
    @Transient
    private Integer checkDoneCount;

    @Schema(description = "已审核图纸数量")
    @Transient
    private Integer reviewDoneCount;

    @Schema(description = "子图纸数量")
    @Transient
    private Integer totalCount;

    @Schema(description = "专业")
    @Column
    private String discipline = "PIPING";

    @Schema(description = "当前执行工序英文名称")
    private String currentProcessNameEn;

    @Schema(description = "图纸传送单名称")
    private String drawingDeliveryFileName;

    @Schema(description = "图纸传送单路径")
    private String drawingDeliveryFilePath;

    @Schema(description = "图纸传送单文件id")
    private Long drawingDeliveryFileId;

    private Boolean isSubDrawing;

    private String areaName;

    private String moduleName;

    @Schema(description = "工作网络")
    private String workNet;

    @Schema(description = "分段")
    private String section;

    @Schema(description = "总段")
    private String block;

    @Schema(description = "小区域")
    private String smallArea;

    @Schema(description = "设计专业")
    private String designDiscipline;

    @Schema(description = "安装/制作图号")
    private String installationDrawingNo;








    public Integer getInitCount() {
        return initCount;
    }

    public void setInitCount(Integer initCount) {
        this.initCount = initCount;
    }

    public Integer getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(Integer checkCount) {
        this.checkCount = checkCount;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Integer getModifyCount() {
        return modifyCount;
    }

    public void setModifyCount(Integer modifyCount) {
        this.modifyCount = modifyCount;
    }

    public Integer getCheckDoneCount() {
        return checkDoneCount;
    }

    public void setCheckDoneCount(Integer checkDoneCount) {
        this.checkDoneCount = checkDoneCount;
    }

    public Integer getReviewDoneCount() {
        return reviewDoneCount;
    }

    public void setReviewDoneCount(Integer reviewDoneCount) {
        this.reviewDoneCount = reviewDoneCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getDwgNo() {
        return dwgNo;
    }

    public void setDwgNo(String dwgNo) {
        this.dwgNo = dwgNo;
    }

    public String getDrawingTitle() {
        return drawingTitle;
    }

    public void setDrawingTitle(String drawingTitle) {
        this.drawingTitle = drawingTitle;
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

    public Long getImportFileId() {
        return importFileId;
    }

    public void setImportFileId(Long importFileId) {
        this.importFileId = importFileId;
    }

    public Long getBatchTaskId() {
        return batchTaskId;
    }

    public void setBatchTaskId(Long batchTaskId) {
        this.batchTaskId = batchTaskId;
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

    public int getFileLastVersion() {
        return fileLastVersion;
    }

    public void setFileLastVersion(int fileLastVersion) {
        this.fileLastVersion = fileLastVersion;
    }

    public String getFilePageCount() {
        return filePageCount;
    }

    public void setFilePageCount(String filePageCount) {
        this.filePageCount = filePageCount;
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

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Long getCoverId() {
        return coverId;
    }

    public void setCoverId(Long coverId) {
        this.coverId = coverId;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getCoverName() {
        return coverName;
    }

    public void setCoverName(String coverName) {
        this.coverName = coverName;
    }

    @Transient
    public String getDiagramResource() {
        return diagramResource;
    }

    public void setDiagramResource(String diagramResource) {
        this.diagramResource = diagramResource;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getActuralDrawingIssueDate() {
        return acturalDrawingIssueDate;
    }

    public void setActuralDrawingIssueDate(Date acturalDrawingIssueDate) {
        this.acturalDrawingIssueDate = acturalDrawingIssueDate;
    }

    public Date getProductionReceivingDate() {
        return productionReceivingDate;
    }

    public void setProductionReceivingDate(Date productionReceivingDate) {
        this.productionReceivingDate = productionReceivingDate;
    }

    public String getIssueCardNo() {
        return issueCardNo;
    }

    public void setIssueCardNo(String issueCardNo) {
        this.issueCardNo = issueCardNo;
    }

    public String getReturnRecord() {
        return returnRecord;
    }

    public void setReturnRecord(String returnRecord) {
        this.returnRecord = returnRecord;
    }

    public String getDesignChangeReviewForm() {
        return designChangeReviewForm;
    }

    public void setDesignChangeReviewForm(String designChangeReviewForm) {
        this.designChangeReviewForm = designChangeReviewForm;
    }

    public String getChangeNoticeNo() {
        return changeNoticeNo;
    }

    public void setChangeNoticeNo(String changeNoticeNo) {
        this.changeNoticeNo = changeNoticeNo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrinting() {
        return printing;
    }

    public void setPrinting(Integer printing) {
        this.printing = printing;
    }

    public float getPaperUse() {
        return paperUse;
    }

    public void setPaperUse(Float paperUse) {
        this.paperUse = paperUse;
    }

    public Float getPaperAmount() {
        return paperAmount;
    }

    public void setPaperAmount(Float paperAmount) {
        this.paperAmount = paperAmount;
    }

    public String getAuditNo() {
        return auditNo;
    }

    public void setAuditNo(String auditNo) {
        this.auditNo = auditNo;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public boolean isActInst() {
        return actInst;
    }

    public void setActInst(boolean actInst) {
        this.actInst = actInst;
    }

    public Date getEngineeringFinishDate() {
        return engineeringFinishDate;
    }

    public void setEngineeringFinishDate(Date engineeringFinishDate) {
        this.engineeringFinishDate = engineeringFinishDate;
    }

    public Date getEngineeringStartDate() {
        return engineeringStartDate;
    }

    public void setEngineeringStartDate(Date engineeringStartDate) {
        this.engineeringStartDate = engineeringStartDate;
    }

    public String getDrawer() {
        return drawer;
    }

    public void setDrawer(String drawer) {
        this.drawer = drawer;
    }

    public Long getDrawerId() {
        return drawerId;
    }

    public void setDrawerId(Long drawerId) {
        this.drawerId = drawerId;
    }

    public Double getEstimatedManHours() {
        return estimatedManHours;
    }

    public void setEstimatedManHours(Double estimatedManHours) {
        this.estimatedManHours = estimatedManHours;
    }

    public String getDrawUsername() {
        return drawUsername;
    }

    public void setDrawUsername(String drawUsername) {
        this.drawUsername = drawUsername;
    }

    public String getCheckUsername() {
        return checkUsername;
    }

    public void setCheckUsername(String checkUsername) {
        this.checkUsername = checkUsername;
    }

    public String getApprovedUsername() {
        return approvedUsername;
    }

    public void setApprovedUsername(String approvedUsername) {
        this.approvedUsername = approvedUsername;
    }

    @JsonProperty(value = "drawUserId", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getDrawUserIdReference() {
        return this.drawUserId == null ? null : new ReferenceData(this.drawUserId);
    }

    @JsonProperty(value = "checkUserId", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getCheckUserIdReference() {
        return this.checkUserId == null ? null : new ReferenceData(this.checkUserId);
    }

    @JsonProperty(value = "approvedUserId", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getApprovedUserIdReference() {
        return this.approvedUserId == null ? null : new ReferenceData(this.approvedUserId);
    }

    @Override
    public Set<Long> relatedUserIDs() {
        Set<Long> relatedUserIDs = new HashSet<>();
        relatedUserIDs.add(this.drawUserId);
        relatedUserIDs.add(this.checkUserId);
        relatedUserIDs.add(this.approvedUserId);
        return relatedUserIDs;
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

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getLatestRev() {
        return latestRev;
    }

    public void setLatestRev(String latestRev) {
        this.latestRev = latestRev;
    }

    public String getCurrentProcessNameEn() {
        return currentProcessNameEn;
    }

    public void setCurrentProcessNameEn(String currentProcessNameEn) {
        this.currentProcessNameEn = currentProcessNameEn;
    }

    public Boolean getRedMarkOnGoing() {
        return isRedMarkOnGoing;
    }

    public void setRedMarkOnGoing(Boolean redMarkOnGoing) {
        isRedMarkOnGoing = redMarkOnGoing;
    }

    public String getDrawingDeliveryFileName() {
        return drawingDeliveryFileName;
    }

    public void setDrawingDeliveryFileName(String drawingDeliveryFileName) {
        this.drawingDeliveryFileName = drawingDeliveryFileName;
    }

    public String getDrawingDeliveryFilePath() {
        return drawingDeliveryFilePath;
    }

    public void setDrawingDeliveryFilePath(String drawingDeliveryFilePath) {
        this.drawingDeliveryFilePath = drawingDeliveryFilePath;
    }

    public Long getDrawingDeliveryFileId() {
        return drawingDeliveryFileId;
    }

    public void setDrawingDeliveryFileId(Long drawingDeliveryFileId) {
        this.drawingDeliveryFileId = drawingDeliveryFileId;
    }

    public String getLatestApprovedRev() {
        return latestApprovedRev;
    }

    public void setLatestApprovedRev(String latestApprovedRev) {
        this.latestApprovedRev = latestApprovedRev;
    }

    public Boolean getSubDrawing() {
        return isSubDrawing;
    }

    public void setSubDrawing(Boolean subDrawing) {
        isSubDrawing = subDrawing;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public Boolean getInfoPut() {
        return isInfoPut;
    }

    public void setInfoPut(Boolean infoPut) {
        isInfoPut = infoPut;
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

    public String getInstallationDrawingNo() {
        return installationDrawingNo;
    }

    public void setInstallationDrawingNo(String installationDrawingNo) {
        this.installationDrawingNo = installationDrawingNo;
    }
}
