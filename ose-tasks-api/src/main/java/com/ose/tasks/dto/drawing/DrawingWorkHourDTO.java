package com.ose.tasks.dto.drawing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.vo.DrawingStatus;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DrawingWorkHourDTO extends Drawing {

    private static final long serialVersionUID = -8574840544739539014L;

    private boolean actInst = false;

    private Long id;

    private Long orgId;

    private Long projectId;

    private Long importFileId;

    private Long batchTaskId;

    private String qrCode;

    //序号
    private String sequenceNo;

    //图号
    private String dwgNo;

    //文件名称
    private String drawingTitle;

    //预估工时
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
    private Boolean isRedMarkOnGoing;

    //图纸类型
    @ManyToOne(fetch = FetchType.LAZY)
    private BpmEntitySubType drawingCategory;

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

    private EntityStatus status;

    @Transient
    private String diagramResource;

    //制图人
    private String drawer;

    //制图人id
    private Long drawerId;

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
    private String discipline;

    @Schema(description = "预算工时")
    @Column
    private Integer budgetManHour;

    @Schema(description = "当前执行工序英文名称")
    private String currentProcessNameEn;

    @Schema(description = "图纸传送单名称")
    private String drawingDeliveryFileName;

    @Schema(description = "图纸传送单路径")
    private String drawingDeliveryFilePath;

    @Schema(description = "图纸传送单文件id")
    private Long drawingDeliveryFileId;


    //数字化测试新增字段
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


    @Schema(description = "文档标题")
    private String documentTitle;

    @Schema(description = "documentChain")
    private String documentChain;

    @Schema(description = "chainCode")
    private String chainCode;

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

    @Schema(description = "dcrNo")
    private String dcrNo;

    @Schema(description = "dcrOutgoingDate")
    private Date dcrOutgoingDate;

    @Schema(description = "dcrReplyDate")
    private Date dcrReplyDate;

    @Schema(description = "dcrRequest")
    private String dcrRequest;

    @Schema(description = "dcrStatus")
    private String dcrStatus;

    @Schema(description = "forEniSubmission")
    private Boolean forEniSubmission;

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

    @Schema(description = "idcCheck")
    private Boolean idcCheck;

    @Schema(description = "idcSponsor")
    private String idcSponsor;

    @Schema(description = "idcDate")
    private Date idcDate;

    @Schema(description = "planStartDate")
    private Date planStartDate;

    @Schema(description = "planIDCDate")
    private Date planIDCDate;

    @Schema(description = "planIFUDate")
    private Date planIFUDate;

    @Schema(description = "planIFRDate")
    private Date planIFRDate;

    @Schema(description = "planIFADate")
    private Date planIFADate;

    @Schema(description = "planAFCDate")
    private Date planAFCDate;

    @Schema(description = "planIFDDate")
    private Date planIFDDate;

    @Schema(description = "planIFCDate")
    private Date planIFCDate;

    @Schema(description = "planIFIDate")
    private Date planIFIDate;

    @Schema(description = "planIABDate")
    private Date planIABDate;

    @Schema(description = "forecastStartDate")
    private Date forecastStartDate;

    @Schema(description = "forecastIFRDate")
    private Date forecastIFRDate;

    @Schema(description = "forecastIFDDate")
    private Date forecastIFDDate;

    @Schema(description = "forecastIDCDate")
    private Date forecastIDCDate;

    @Schema(description = "forecastIFUDate")
    private Date forecastIFUDate;

    @Schema(description = "forecastIFCDate")
    private Date forecastIFCDate;

    @Schema(description = "forecastIFADate")
    private Date forecastIFADate;

    @Schema(description = "forecastAFCDate")
    private Date forecastAFCDate;

    @Schema(description = "forecastIFIDate")
    private Date forecastIFIDate;

    @Schema(description = "forecastIABDate")
    private Date forecastIABDate;



    @Schema(description = "actualStartDate")
    private Date actualStartDate;

    @Schema(description = "actualIDCDate")
    private Date actualIDCDate;

    @Schema(description = "actualIFUDate")
    private Date actualIFUDate;

    @Schema(description = "actualIFRDate")
    private Date actualIFRDate;

    @Schema(description = "actualIFADate")
    private Date actualIFADate;

    @Schema(description = "actualAFCDate")
    private Date actualAFCDate;

    @Schema(description = "actualIFRDate")
    private Date actualIFIDate;

    private Long stageId;

    private String process;

    private Long processId;

    private String rev;
    //数字化测试新增字段
    @Column(name = "vp_id")
    private Long vpId;

    @Column(name = "vp")
    private String vp;

    @Column(name = "sh_id")
    private Long shId;

    @Column(name = "sh")
    private String sh;


    @Column()
    private Long leadEngineerId;

    @Column()
    private String leadEngineer;


    @Column()
    private Long engineerId;

    private String engineer;

    private String issuedRev;

    private String issuedDesigner;

    private String unIssuedRev;

    private String unIssuedDesigner;

    private String currentProcess;

    private String currentExecutor;

    private String drawingType;


    @Column()
    private Double workHour = 0.0;

    private DrawingStatus drawingStatus;



    @Schema(description = "delayPlanStart")
    private String delayPlanStart;
    @Schema(description = "delayForecastStart")
    private String delayForecastStart;
    @Schema(description = "delayPlanIDC")
    private String delayPlanIDC;
    @Schema(description = "delayForecastIDC")
    private String delayForecastIDC;
    @Schema(description = "delayPlanIFR")
    private String delayPlanIFR;
    @Schema(description = "delayForecastIFR")
    private String delayForecastIFR;
    @Schema(description = "delayPlanIFU")
    private String delayPlanIFU;
    @Schema(description = "delayForecastIFU")
    private String delayForecastIFU;
    @Schema(description = "delayPlanIFA")
    private String delayPlanIFA;
    @Schema(description = "delayForecastIFA")
    private String delayForecastIFA;
    @Schema(description = "delayPlanAFC")
    private String delayPlanAFC;
    @Schema(description = "delayForecastAFC")
    private String delayForecastAFC;
    @Schema(description = "delayPlanIFI")
    private String delayPlanIFI;
    @Schema(description = "delayForecastIFI")
    private String delayForecastIFI;

    public Date getActualIFADate() {
        return actualIFADate;
    }

    public void setActualIFADate(Date actualIFADate) {
        this.actualIFADate = actualIFADate;
    }

    public Date getActualAFCDate() {
        return actualAFCDate;
    }

    public void setActualAFCDate(Date actualAFCDate) {
        this.actualAFCDate = actualAFCDate;
    }

    public Date getActualIFIDate() {
        return actualIFIDate;
    }

    public void setActualIFIDate(Date actualIFIDate) {
        this.actualIFIDate = actualIFIDate;
    }

    public String getDelayPlanIFA() {
        return delayPlanIFA;
    }

    public void setDelayPlanIFA(String delayPlanIFA) {
        this.delayPlanIFA = delayPlanIFA;
    }

    public String getDelayForecastIFA() {
        return delayForecastIFA;
    }

    public void setDelayForecastIFA(String delayForecastIFA) {
        this.delayForecastIFA = delayForecastIFA;
    }

    public String getDelayPlanAFC() {
        return delayPlanAFC;
    }

    public void setDelayPlanAFC(String delayPlanAFC) {
        this.delayPlanAFC = delayPlanAFC;
    }

    public String getDelayForecastAFC() {
        return delayForecastAFC;
    }

    public void setDelayForecastAFC(String delayForecastAFC) {
        this.delayForecastAFC = delayForecastAFC;
    }

    public String getDelayPlanIFI() {
        return delayPlanIFI;
    }

    public void setDelayPlanIFI(String delayPlanIFI) {
        this.delayPlanIFI = delayPlanIFI;
    }

    public String getDelayForecastIFI() {
        return delayForecastIFI;
    }

    public void setDelayForecastIFI(String delayForecastIFI) {
        this.delayForecastIFI = delayForecastIFI;
    }

    public String getDelayPlanStart() {
        return delayPlanStart;
    }

    public void setDelayPlanStart(String delayPlanStart) {
        this.delayPlanStart = delayPlanStart;
    }

    public String getDelayForecastStart() {
        return delayForecastStart;
    }

    public void setDelayForecastStart(String delayForecastStart) {
        this.delayForecastStart = delayForecastStart;
    }

    public String getDelayPlanIDC() {
        return delayPlanIDC;
    }

    public void setDelayPlanIDC(String delayPlanIDC) {
        this.delayPlanIDC = delayPlanIDC;
    }

    public String getDelayForecastIDC() {
        return delayForecastIDC;
    }

    public void setDelayForecastIDC(String delayForecastIDC) {
        this.delayForecastIDC = delayForecastIDC;
    }

    public String getDelayPlanIFR() {
        return delayPlanIFR;
    }

    public void setDelayPlanIFR(String delayPlanIFR) {
        this.delayPlanIFR = delayPlanIFR;
    }

    public String getDelayForecastIFR() {
        return delayForecastIFR;
    }

    public void setDelayForecastIFR(String delayForecastIFR) {
        this.delayForecastIFR = delayForecastIFR;
    }

    public String getDelayPlanIFU() {
        return delayPlanIFU;
    }

    public void setDelayPlanIFU(String delayPlanIFU) {
        this.delayPlanIFU = delayPlanIFU;
    }

    public String getDelayForecastIFU() {
        return delayForecastIFU;
    }

    public void setDelayForecastIFU(String delayForecastIFU) {
        this.delayForecastIFU = delayForecastIFU;
    }

    public DrawingStatus getDrawingStatus() {
        return drawingStatus;
    }

    public void setDrawingStatus(DrawingStatus drawingStatus) {
        this.drawingStatus = drawingStatus;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVpId() {
        return vpId;
    }

    public void setVpId(Long vpId) {
        this.vpId = vpId;
    }

    public String getVp() {
        return vp;
    }

    public void setVp(String vp) {
        this.vp = vp;
    }

    public Long getShId() {
        return shId;
    }

    public void setShId(Long shId) {
        this.shId = shId;
    }

    public String getSh() {
        return sh;
    }

    public void setSh(String sh) {
        this.sh = sh;
    }

    public Long getLeadEngineerId() {
        return leadEngineerId;
    }

    public void setLeadEngineerId(Long leadEngineerId) {
        this.leadEngineerId = leadEngineerId;
    }

    public String getLeadEngineer() {
        return leadEngineer;
    }

    public void setLeadEngineer(String leadEngineer) {
        this.leadEngineer = leadEngineer;
    }

    public Long getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(Long engineerId) {
        this.engineerId = engineerId;
    }

    public String getEngineer() {
        return engineer;
    }

    public void setEngineer(String engineer) {
        this.engineer = engineer;
    }

    public Double getWorkHour() {
        return workHour;
    }

    public void setWorkHour(Double workHour) {
        this.workHour = workHour;
    }

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
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

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
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

    public String getChainCode() {
        return chainCode;
    }

    public void setChainCode(String chainCode) {
        this.chainCode = chainCode;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
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

    public BpmEntitySubType getDrawingCategory() {
        return drawingCategory;
    }

    public void setDrawingCategory(BpmEntitySubType drawingCategory) {
        this.drawingCategory = drawingCategory;
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

    public Integer getBudgetManHour() {
        return budgetManHour;
    }

    public void setBudgetManHour(Integer budgetManHour) {
        this.budgetManHour = budgetManHour;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getOriginatorName() {
        return originatorName;
    }

    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName;
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

    public Boolean getIdcCheck() {
        return idcCheck;
    }

    public void setIdcCheck(Boolean idcCheck) {
        this.idcCheck = idcCheck;
    }

    public String getIdcSponsor() {
        return idcSponsor;
    }

    public void setIdcSponsor(String idcSponsor) {
        this.idcSponsor = idcSponsor;
    }

    public Date getIdcDate() {
        return idcDate;
    }

    public void setIdcDate(Date idcDate) {
        this.idcDate = idcDate;
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

    public Boolean getForEniSubmission() {
        return forEniSubmission;
    }

    public void setForEniSubmission(Boolean forEniSubmission) {
        this.forEniSubmission = forEniSubmission;
    }

    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }

    public Date getPlanIFRDate() {
        return planIFRDate;
    }

    public void setPlanIFRDate(Date planIFRDate) {
        this.planIFRDate = planIFRDate;
    }

    public Date getPlanIFDDate() {
        return planIFDDate;
    }

    public void setPlanIFDDate(Date planIFDDate) {
        this.planIFDDate = planIFDDate;
    }

    public Date getPlanIFCDate() {
        return planIFCDate;
    }

    public void setPlanIFCDate(Date planIFCDate) {
        this.planIFCDate = planIFCDate;
    }

    public Date getPlanIFIDate() {
        return planIFIDate;
    }

    public void setPlanIFIDate(Date planIFIDate) {
        this.planIFIDate = planIFIDate;
    }

    public Date getPlanIABDate() {
        return planIABDate;
    }

    public void setPlanIABDate(Date planIABDate) {
        this.planIABDate = planIABDate;
    }

    public Date getForecastStartDate() {
        return forecastStartDate;
    }

    public void setForecastStartDate(Date forecastStartDate) {
        this.forecastStartDate = forecastStartDate;
    }

    public Date getForecastIFRDate() {
        return forecastIFRDate;
    }

    public void setForecastIFRDate(Date forecastIFRDate) {
        this.forecastIFRDate = forecastIFRDate;
    }

    public Date getForecastIFDDate() {
        return forecastIFDDate;
    }

    public void setForecastIFDDate(Date forecastIFDDate) {
        this.forecastIFDDate = forecastIFDDate;
    }

    public Date getForecastIFCDate() {
        return forecastIFCDate;
    }

    public void setForecastIFCDate(Date forecastIFCDate) {
        this.forecastIFCDate = forecastIFCDate;
    }

    public Date getForecastIFIDate() {
        return forecastIFIDate;
    }

    public void setForecastIFIDate(Date forecastIFIDate) {
        this.forecastIFIDate = forecastIFIDate;
    }

    public Date getForecastIABDate() {
        return forecastIABDate;
    }

    public void setForecastIABDate(Date forecastIABDate) {
        this.forecastIABDate = forecastIABDate;
    }

    public String getDcrNo() {
        return dcrNo;
    }

    public void setDcrNo(String dcrNo) {
        this.dcrNo = dcrNo;
    }

    public String getDcrStatus() {
        return dcrStatus;
    }

    public void setDcrStatus(String dcrStatus) {
        this.dcrStatus = dcrStatus;
    }

    public Date getDcrReplyDate() {
        return dcrReplyDate;
    }

    public void setDcrReplyDate(Date dcrReplyDate) {
        this.dcrReplyDate = dcrReplyDate;
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

    public Date getDcrOutgoingDate() {
        return dcrOutgoingDate;
    }

    public void setDcrOutgoingDate(Date dcrOutgoingDate) {
        this.dcrOutgoingDate = dcrOutgoingDate;
    }

    public String getDcrRequest() {
        return dcrRequest;
    }

    public void setDcrRequest(String dcrRequest) {
        this.dcrRequest = dcrRequest;
    }

    public String getIssuedRev() {
        return issuedRev;
    }

    public void setIssuedRev(String issuedRev) {
        this.issuedRev = issuedRev;
    }

    public String getIssuedDesigner() {
        return issuedDesigner;
    }

    public void setIssuedDesigner(String issuedDesigner) {
        this.issuedDesigner = issuedDesigner;
    }

    public String getUnIssuedRev() {
        return unIssuedRev;
    }

    public void setUnIssuedRev(String unIssuedRev) {
        this.unIssuedRev = unIssuedRev;
    }

    public String getUnIssuedDesigner() {
        return unIssuedDesigner;
    }

    public void setUnIssuedDesigner(String unIssuedDesigner) {
        this.unIssuedDesigner = unIssuedDesigner;
    }

    public String getCurrentProcess() {
        return currentProcess;
    }

    public void setCurrentProcess(String currentProcess) {
        this.currentProcess = currentProcess;
    }

    public String getCurrentExecutor() {
        return currentExecutor;
    }

    public void setCurrentExecutor(String currentExecutor) {
        this.currentExecutor = currentExecutor;
    }

    public Date getPlanIDCDate() {
        return planIDCDate;
    }

    public void setPlanIDCDate(Date planIDCDate) {
        this.planIDCDate = planIDCDate;
    }

    public Date getPlanIFUDate() {
        return planIFUDate;
    }

    public void setPlanIFUDate(Date planIFUDate) {
        this.planIFUDate = planIFUDate;
    }

    public Date getForecastIDCDate() {
        return forecastIDCDate;
    }

    public void setForecastIDCDate(Date forecastIDCDate) {
        this.forecastIDCDate = forecastIDCDate;
    }

    public Date getForecastIFUDate() {
        return forecastIFUDate;
    }

    public void setForecastIFUDate(Date forecastIFUDate) {
        this.forecastIFUDate = forecastIFUDate;
    }

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Date getActualIDCDate() {
        return actualIDCDate;
    }

    public void setActualIDCDate(Date actualIDCDate) {
        this.actualIDCDate = actualIDCDate;
    }

    public Date getActualIFUDate() {
        return actualIFUDate;
    }

    public void setActualIFUDate(Date actualIFUDate) {
        this.actualIFUDate = actualIFUDate;
    }

    public Date getActualIFRDate() {
        return actualIFRDate;
    }

    public void setActualIFRDate(Date actualIFRDate) {
        this.actualIFRDate = actualIFRDate;
    }

    @Override
    public String getDrawingType() {
        return drawingType;
    }

    @Override
    public void setDrawingType(String drawingType) {
        this.drawingType = drawingType;
    }

    @Override
    public String getOwnerDocNo() {
        return ownerDocNo;
    }

    @Override
    public void setOwnerDocNo(String ownerDocNo) {
        this.ownerDocNo = ownerDocNo;
    }

    public Date getPlanIFADate() {
        return planIFADate;
    }

    public void setPlanIFADate(Date planIFADate) {
        this.planIFADate = planIFADate;
    }

    public Date getPlanAFCDate() {
        return planAFCDate;
    }

    public void setPlanAFCDate(Date planAFCDate) {
        this.planAFCDate = planAFCDate;
    }

    public Date getForecastIFADate() {
        return forecastIFADate;
    }

    public void setForecastIFADate(Date forecastIFADate) {
        this.forecastIFADate = forecastIFADate;
    }

    public Date getForecastAFCDate() {
        return forecastAFCDate;
    }

    public void setForecastAFCDate(Date forecastAFCDate) {
        this.forecastAFCDate = forecastAFCDate;
    }
}
