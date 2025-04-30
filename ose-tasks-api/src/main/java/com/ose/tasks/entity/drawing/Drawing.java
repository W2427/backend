package com.ose.tasks.entity.drawing;

import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.wbs.entity.WorkflowProcessVariable;
import com.ose.vo.DrawingStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 图集
 */
@Entity
@Table(name = "drawing",
    indexes = {
        @Index(columnList = "orgId,projectId")
    })
public class Drawing extends WBSEntityBase implements WorkflowProcessVariable {

    /**
     *
     */
    private static final long serialVersionUID = -7837891255273680991L;

    @Schema(description = "是否有正在运行的图纸流程")
    @Transient
    private boolean actInst = false;

    @Schema(description = "组织Id")
    private Long orgId;

    @Schema(description = "项目Id")
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

    private String contractCode;

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

    @Transient
    private boolean detailDwgCanBeAdded = true;

    @Schema(description = "是否 有 RED-MARK 流程在运行")
    @Column
    private Boolean isRedMarkOnGoing;

    @Schema(description = "review chain")
    @Column
    private int reviewChain;

    public int getReviewChain() {
        return reviewChain;
    }

    public void setReviewChain(int reviewChain) {
        this.reviewChain = reviewChain;
    }

    @Schema(description = "审批通过")
    @Column
    private String reviewResult;

    public String getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(String reviewResult) {
        this.reviewResult = reviewResult;
    }

    //图纸类型
    private String entitySubType;

    //图纸是否被锁定，只有在设计环节不是锁定的，false
    private boolean locked = true;

    //图纸封面文件id
    private Long coverId;

    //图纸封面文件Name
    private String coverName;

    //图纸封面文件路径
    private String coverPath;

    @Transient
    private String diagramResource;

    //制图人
    @Transient
    private String preparePerson;

    private Long preparePersonId;


    @Schema(description = "设计人员用户名")
    @Transient
    private String reviewPerson;

    private Long reviewPersonId;

    @Schema(description = "审核人员用户名")
    @Transient
    private String approvePerson;

    private Long approvePersonId;

    @Schema(description = "会签人员用户名")
    @Transient
    private String coSignPerson;

    @Schema(description = "qc人员用户名")
    @Transient
    private String qcPerson;

    @Schema(description = "dwg人员用户名")
    @Transient
    private String docPerson;

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


    @Column(columnDefinition = "decimal(9,1) default 0.0")
    @Schema(description = "计划工时")
    private Double planHours;

    @Schema(description = "图纸状态")
    @Enumerated(EnumType.STRING)
    private DrawingStatus drawingStatus;

    // 2023.07.17 重新整理字段-----CQ
    @Schema(description = "sdrl编号")
    private String sdrlCode;

    @Schema(description = "包编号")
    private String packageNo;

    @Schema(description = "包名称")
    private String packageName;

    @Schema(description = "originatorName")
    private String originatorName;

    @Schema(description = "项目编号")
    private String projectNo;

    @Schema(description = "orgCode")
    private String orgCode;

    @Schema(description = "systemCode")
    private String systemCode;

    @Schema(description = "discCode")
    private String discCode;

    @Schema(description = "docType")
    private String docType;

    @Schema(description = "shortCode")
    private String shortCode;

    @Schema(description = "sheetNo")
    private String sheetNo;

    private String clientSubmission;

    private String classSubmission;

    @Schema(description = "文档标题")
    private String documentTitle;

    @Schema(description = "documentChain")
    private String documentChain;

    @Schema(description = "chainCode")
    private String chainCode;

    @Schema(description = "clientDocRev")
    private String clientDocRev;

    @Schema(description = "clientDocNo")
    private String clientDocNo;

    @Schema(description = "ownerDocNo")
    private String ownerDocNo;

    @Schema(description = "validityStatus")
    private String validityStatus;

    @Schema(description = "surveillanceType")
    private String surveillanceType;

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    @Schema(description = "图纸区域，TOPSIDE/HULL")
    private String sector;

    @Schema(description = "progressStage")
    private String progressStage;

    @Schema(description = "revNo")
    private String revNo;

    @Schema(description = "progressStage")
    private Date uploadDate;

    @Schema(description = "outgoingTransmittal")
    private String outgoingTransmittal;

    @Schema(description = "incomingTransmittal")
    private String incomingTransmittal;

    @Schema(description = "replyDate")
    private Date replyDate;

    @Schema(description = "replyStatus")
    private String replyStatus;

    @Schema(description = "subDrawingFlg")
    @Transient
    private boolean subDrawingFlg = false;

    private Long fileId;

    private String fileName;

    private String filePath;

    private String drawingType;

    @Transient
    private String pdfUpdateVersion;

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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getDocumentChain() {
        return documentChain;
    }

    public void setDocumentChain(String documentChain) {
        this.documentChain = documentChain;
    }

    public DrawingStatus getDrawingStatus() {
        return drawingStatus;
    }

    public void setDrawingStatus(DrawingStatus drawingStatus) {
        this.drawingStatus = drawingStatus;
    }

    public boolean isActInst() {
        return actInst;
    }

    public void setActInst(boolean actInst) {
        this.actInst = actInst;
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
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

    public String getLatestRev() {
        return latestRev;
    }

    public void setLatestRev(String latestRev) {
        this.latestRev = latestRev;
    }

    public String getLatestApprovedRev() {
        return latestApprovedRev;
    }

    public void setLatestApprovedRev(String latestApprovedRev) {
        this.latestApprovedRev = latestApprovedRev;
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

    public Float getPaperUse() {
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

    public Boolean getRedMarkOnGoing() {
        return isRedMarkOnGoing;
    }

    public void setRedMarkOnGoing(Boolean redMarkOnGoing) {
        isRedMarkOnGoing = redMarkOnGoing;
    }

    @Override
    public String getEntitySubType() {
        return entitySubType;
    }

    @Override
    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
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

    public String getCoverName() {
        return coverName;
    }

    public void setCoverName(String coverName) {
        this.coverName = coverName;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getDiagramResource() {
        return diagramResource;
    }

    public void setDiagramResource(String diagramResource) {
        this.diagramResource = diagramResource;
    }

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

    public String getOriginatorName() {
        return originatorName;
    }

    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
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

    public String getClientDocRev() {
        return clientDocRev;
    }

    public void setClientDocRev(String clientDocRev) {
        this.clientDocRev = clientDocRev;
    }

    public String getClientDocNo() {
        return clientDocNo;
    }

    public void setClientDocNo(String clientDocNo) {
        this.clientDocNo = clientDocNo;
    }

    public String getValidityStatus() {
        return validityStatus;
    }

    public void setValidityStatus(String validityStatus) {
        this.validityStatus = validityStatus;
    }

    public String getSurveillanceType() {
        return surveillanceType;
    }

    public void setSurveillanceType(String surveillanceType) {
        this.surveillanceType = surveillanceType;
    }


    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(String sheetNo) {
        this.sheetNo = sheetNo;
    }

    public String getChainCode() {
        return chainCode;
    }

    public void setChainCode(String chainCode) {
        this.chainCode = chainCode;
    }

    public String getProgressStage() {
        return progressStage;
    }

    public void setProgressStage(String progressStage) {
        this.progressStage = progressStage;
    }

    public String getRevNo() {
        return revNo;
    }

    public void setRevNo(String revNo) {
        this.revNo = revNo;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getOutgoingTransmittal() {
        return outgoingTransmittal;
    }

    public void setOutgoingTransmittal(String outgoingTransmittal) {
        this.outgoingTransmittal = outgoingTransmittal;
    }

    public String getIncomingTransmittal() {
        return incomingTransmittal;
    }

    public void setIncomingTransmittal(String incomingTransmittal) {
        this.incomingTransmittal = incomingTransmittal;
    }

    public Date getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }

    public String getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(String replyStatus) {
        this.replyStatus = replyStatus;
    }

    @Override
    public String getEntityBusinessType() {
        return docType;
    }

    public String getNo() {
        return dwgNo;
    }

    @Override
    public String getVariableName() {
        return "DOC";
    }

    @Override
    public String getName() {
        return null;
    }

    public boolean isDetailDwgCanBeAdded() {
        return detailDwgCanBeAdded;
    }

    public void setDetailDwgCanBeAdded(boolean detailDwgCanBeAdded) {
        this.detailDwgCanBeAdded = detailDwgCanBeAdded;
    }

    public boolean isSubDrawingFlg() {
        return subDrawingFlg;
    }

    public void setSubDrawingFlg(boolean subDrawingFlg) {
        this.subDrawingFlg = subDrawingFlg;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getClientSubmission() {
        return clientSubmission;
    }

    public void setClientSubmission(String clientSubmission) {
        this.clientSubmission = clientSubmission;
    }

    public String getClassSubmission() {
        return classSubmission;
    }

    public void setClassSubmission(String classSubmission) {
        this.classSubmission = classSubmission;
    }

    public String getPreparePerson() {
        return preparePerson;
    }

    public void setPreparePerson(String preparePerson) {
        this.preparePerson = preparePerson;
    }

    public String getReviewPerson() {
        return reviewPerson;
    }

    public void setReviewPerson(String reviewPerson) {
        this.reviewPerson = reviewPerson;
    }

    public String getApprovePerson() {
        return approvePerson;
    }

    public void setApprovePerson(String approvePerson) {
        this.approvePerson = approvePerson;
    }

    public Double getPlanHours() {
        return planHours;
    }

    public void setPlanHours(Double planHours) {
        this.planHours = planHours;
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

    public String getCoSignPerson() {
        return coSignPerson;
    }

    public void setCoSignPerson(String coSignPerson) {
        this.coSignPerson = coSignPerson;
    }

    public String getQcPerson() {
        return qcPerson;
    }

    public void setQcPerson(String qcPerson) {
        this.qcPerson = qcPerson;
    }

    public String getDocPerson() {
        return docPerson;
    }

    public void setDocPerson(String docPerson) {
        this.docPerson = docPerson;
    }

    public String getDrawingType() {
        return drawingType;
    }

    public void setDrawingType(String drawingType) {
        this.drawingType = drawingType;
    }

    public String getPdfUpdateVersion() {
        return pdfUpdateVersion;
    }

    public void setPdfUpdateVersion(String pdfUpdateVersion) {
        this.pdfUpdateVersion = pdfUpdateVersion;
    }

    public String getOwnerDocNo() {
        return ownerDocNo;
    }

    public void setOwnerDocNo(String ownerDocNo) {
        this.ownerDocNo = ownerDocNo;
    }

    public Long getPreparePersonId() {
        return preparePersonId;
    }

    public void setPreparePersonId(Long preparePersonId) {
        this.preparePersonId = preparePersonId;
    }

    public Long getReviewPersonId() {
        return reviewPersonId;
    }

    public void setReviewPersonId(Long reviewPersonId) {
        this.reviewPersonId = reviewPersonId;
    }

    public Long getApprovePersonId() {
        return approvePersonId;
    }

    public void setApprovePersonId(Long approvePersonId) {
        this.approvePersonId = approvePersonId;
    }
}
