package com.ose.tasks.dto.drawing;

import java.util.Date;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 生产设计图纸清单导入数据传输对象。
 */
public class DrawingDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "序号")
    private String sequenceNo;

    @Schema(description = "修改单编号")
    private String no;

    @Schema(description = "图号")
    private String dwgNo;

    @Schema(description = "文件名称")
    private String drawingTitle;

    @Schema(description = "最新版本")
    private String latestRev;

    @Schema(description = "计划提交建造时间")
    private Date deliveryDate;

    @Schema(description = "图纸类型id")
    private Long drawingCategoryId;

    @Schema(description = "临时文件名")
    private String fileName;

    @Schema(description = "文件页数")
    private String filePageCount;

    @Schema(description = "备注")
    private String memo;

    @Schema(description = "封面临时文件名 file Id")
    private String coverTempName;

    @Schema(description = "担当人id")
    private Long assigneeId;

    @Schema(description = "担当人姓名")
    private String assigneeName;

    @Schema(description = "设计开始时间")
    private Date engineeringStartDate;

    @Schema(description = "设计结束时间")
    private Date engineeringFinishDate;

    @Schema(description = "预估工时")
    private Double estimatedManHours;

    @Schema(description = "图纸设计人员id")
    private Long drawUserId;

    @Schema(description = "图纸校对人员id")
    private Long checkUserId;

    @Schema(description = "图纸审核人员id")
    private Long approvedUserId;

    // 整理后的字段
    private String sdrlCode;

    private String packageNo;
    ;

    private String packageName;

    private String originatorName;

    private String projectNo;

    private String orgCode;

    private String systemCode;

    private String discCode;

    private String docType;

    private String shortCode;

    private String sheetNo;


    private String documentTitle;

    private String documentChain;

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

    private Boolean forEniSubmission;

    private String clientDocRev;

    private String clientDocNo;

    private String validityStatus;

    private String surveillanceType;

    private Boolean idcCheck;

    private String idcSponsor;

    private Date idcDate;

    private Date planStartDate;

    private Date planIFRDate;

    private Date planIFDDate;

    private Date planIFCDate;

    private Date planIFIDate;

    private Date planIABDate;

    private Date forecastStartDate;

    private Date forecastIFRDate;

    private Date forecastIFDDate;

    private Date forecastIFCDate;

    private Date forecastIFIDate;

    private Date forecastIABDate;

    @Schema(description = "预算工时")
    private Integer budgetManHour;

    private String funcPart;

    private String stage;

    private String process;

    private String version;

    @Schema(description = "区域号")
    private String areaName;

    @Schema(description = "模块号")
    private String moduleName;

    @Schema(description = "信息处理")
    private String infoPut;

    @Schema(description = "临时")
    private String token;

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

    private String newVersion;

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getBudgetManHour() {
        return budgetManHour;
    }

    public void setBudgetManHour(Integer budgetManHour) {
        this.budgetManHour = budgetManHour;
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

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Long getDrawingCategoryId() {
        return drawingCategoryId;
    }

    public void setDrawingCategoryId(Long drawingCategoryId) {
        this.drawingCategoryId = drawingCategoryId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getCoverTempName() {
        return coverTempName;
    }

    public void setCoverTempName(String coverTempName) {
        this.coverTempName = coverTempName;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public Date getEngineeringStartDate() {
        return engineeringStartDate;
    }

    public void setEngineeringStartDate(Date engineeringStartDate) {
        this.engineeringStartDate = engineeringStartDate;
    }

    public Date getEngineeringFinishDate() {
        return engineeringFinishDate;
    }

    public void setEngineeringFinishDate(Date engineeringFinishDate) {
        this.engineeringFinishDate = engineeringFinishDate;
    }

    public Double getEstimatedManHours() {
        return estimatedManHours;
    }

    public void setEstimatedManHours(Double estimatedManHours) {
        this.estimatedManHours = estimatedManHours;
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

    public String getLatestRev() {
        return latestRev;
    }

    public void setLatestRev(String latestRev) {
        this.latestRev = latestRev;
    }

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getOriginatorName() {
        return originatorName;
    }

    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName;
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

    public Boolean getForEniSubmission() {
        return forEniSubmission;
    }

    public void setForEniSubmission(Boolean forEniSubmission) {
        this.forEniSubmission = forEniSubmission;
    }

    public Date getIdcDate() {
        return idcDate;
    }

    public void setIdcDate(Date idcDate) {
        this.idcDate = idcDate;
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

    public String getDcrNo() {
        return dcrNo;
    }

    public void setDcrNo(String dcrNo) {
        this.dcrNo = dcrNo;
    }

    public Date getDcrOutgoingDate() {
        return dcrOutgoingDate;
    }

    public void setDcrOutgoingDate(Date dcrOutgoingDate) {
        this.dcrOutgoingDate = dcrOutgoingDate;
    }

    public Date getDcrReplyDate() {
        return dcrReplyDate;
    }

    public void setDcrReplyDate(Date dcrReplyDate) {
        this.dcrReplyDate = dcrReplyDate;
    }

    public String getDcrRequest() {
        return dcrRequest;
    }

    public void setDcrRequest(String dcrRequest) {
        this.dcrRequest = dcrRequest;
    }

    public String getDcrStatus() {
        return dcrStatus;
    }

    public void setDcrStatus(String dcrStatus) {
        this.dcrStatus = dcrStatus;
    }
}
