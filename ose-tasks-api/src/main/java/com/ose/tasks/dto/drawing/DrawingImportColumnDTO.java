package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;

/**
 * 生产设计图纸清单导入数据 列设定 传输对象。
 */
public class DrawingImportColumnDTO extends BaseDTO {


    private static final long serialVersionUID = -8676764584328029730L;

    //设计人员
    private Integer designUserColumn;

    //校验人员
    private Integer checkUserColumn;

    //审核人员
    private Integer approveUserColumn;

    //图纸序号
    private Integer seqNoColumn;

    //dwgNo
    private Integer dwgNoColumn;

    //图纸最新版本号
    private Integer latestRevColumn;

    //dwg Title
    private Integer dwgTitleColumn;

    //ratedHours normalHours
    private Integer ratedHoursColumn;

    //plan start
    private Integer planStartColumn;

    //plan end
    private Integer planEndColumn;


    // 2023-07-17 重新整理字段---CQ
    private Integer sdrlCodeColumn;

    private Integer packageNoColumn;

    private Integer packageNameColumn;

    private Integer originatorNameColumn;

    private Integer projectNoColumn;

    private Integer orgCodeColumn;

    private Integer systemCodeColumn;

    private Integer discCodeColumn;

    private Integer docTypeColumn;

    private Integer shortCodeColumn;

    private Integer sheetNoColumn;


    private Integer documentTitleColumn;

    private Integer documentChainColumn;

    private Integer chainCodeColumnColumn;

    private Integer progressStageColumn;

    private Integer revNoColumn;

    private Integer uploadDateColumn;

    private Integer outgoingTransmittalColumn;

    private Integer incomingTransmittalColumn;

    private Integer replyDateColumn;

    private Integer replyStatusColumn;

    private Integer dcrNoColumn;

    private Integer dcrOutgoingDateColumn;

    private Integer dcrReplyDateColumn;

    private Integer dcrRequestColumn;

    private Integer dcrStatusColumn;

    private Integer dcrSubmissionDateColumn;

    private Integer forEniSubmissionColumn;

    private Integer clientDocRevColumn;

    private Integer clientDocNoColumn;

    private Integer validityStatusColumn;

    private Integer surveillanceTypeColumn;

    private Integer idcCheckColumn;

    private Integer idcSponsorColumn;

    private Integer idcDateColumn;

    private Integer planStartDateColumn;

    private Integer planIFRDateColumn;

    private Integer planIFDDateColumn;

    private Integer planIFCDateColumn;

    private Integer planIFIDateColumn;

    private Integer planIABDateColumn;

    private Integer forecastStartDateColumn;

    private Integer forecastIFRDateColumn;

    private Integer forecastIFDDateColumn;

    private Integer forecastIFCDateColumn;

    private Integer forecastIFIDateColumn;

    private Integer forecastIABDateColumn;

    private Integer disciplineColumn;

    private Integer budgetManHourColumn;

    private Integer deleteFlagColumn;




    // external所添字段
    private Integer areaName;

    private Integer moduleName;

    private Integer isSubDrawing;

    private Integer drawingCategory;

    private Integer workNet;

    private Integer section;

    private Integer block;

    private Integer smallArea;

    private Integer designDiscipline;

    private Integer installationDrawingNo;

    private Integer amendmentNo;

    public Integer getDesignUserColumn() {
        return designUserColumn;
    }

    public void setDesignUserColumn(Integer designUserColumn) {
        this.designUserColumn = designUserColumn;
    }

    public Integer getCheckUserColumn() {
        return checkUserColumn;
    }

    public void setCheckUserColumn(Integer checkUserColumn) {
        this.checkUserColumn = checkUserColumn;
    }

    public Integer getApproveUserColumn() {
        return approveUserColumn;
    }

    public void setApproveUserColumn(Integer approveUserColumn) {
        this.approveUserColumn = approveUserColumn;
    }

    public Integer getSeqNoColumn() {
        return seqNoColumn;
    }

    public void setSeqNoColumn(Integer seqNoColumn) {
        this.seqNoColumn = seqNoColumn;
    }

    public Integer getDwgNoColumn() {
        return dwgNoColumn;
    }

    public void setDwgNoColumn(Integer dwgNoColumn) {
        this.dwgNoColumn = dwgNoColumn;
    }

    public Integer getLatestRevColumn() {
        return latestRevColumn;
    }

    public void setLatestRevColumn(Integer latestRevColumn) {
        this.latestRevColumn = latestRevColumn;
    }

    public Integer getDwgTitleColumn() {
        return dwgTitleColumn;
    }

    public void setDwgTitleColumn(Integer dwgTitleColumn) {
        this.dwgTitleColumn = dwgTitleColumn;
    }

    public Integer getRatedHoursColumn() {
        return ratedHoursColumn;
    }

    public void setRatedHoursColumn(Integer ratedHoursColumn) {
        this.ratedHoursColumn = ratedHoursColumn;
    }

    public Integer getPlanStartColumn() {
        return planStartColumn;
    }

    public void setPlanStartColumn(Integer planStartColumn) {
        this.planStartColumn = planStartColumn;
    }

    public Integer getPlanEndColumn() {
        return planEndColumn;
    }

    public void setPlanEndColumn(Integer planEndColumn) {
        this.planEndColumn = planEndColumn;
    }

    public Integer getSdrlCodeColumn() {
        return sdrlCodeColumn;
    }

    public void setSdrlCodeColumn(Integer sdrlCodeColumn) {
        this.sdrlCodeColumn = sdrlCodeColumn;
    }

    public Integer getPackageNoColumn() {
        return packageNoColumn;
    }

    public void setPackageNoColumn(Integer packageNoColumn) {
        this.packageNoColumn = packageNoColumn;
    }

    public Integer getPackageNameColumn() {
        return packageNameColumn;
    }

    public void setPackageNameColumn(Integer packageNameColumn) {
        this.packageNameColumn = packageNameColumn;
    }

    public Integer getProjectNoColumn() {
        return projectNoColumn;
    }

    public void setProjectNoColumn(Integer projectNoColumn) {
        this.projectNoColumn = projectNoColumn;
    }

    public Integer getDocumentTitleColumn() {
        return documentTitleColumn;
    }

    public void setDocumentTitleColumn(Integer documentTitleColumn) {
        this.documentTitleColumn = documentTitleColumn;
    }

    public Integer getDocumentChainColumn() {
        return documentChainColumn;
    }

    public void setDocumentChainColumn(Integer documentChainColumn) {
        this.documentChainColumn = documentChainColumn;
    }

    public Integer getDisciplineColumn() {
        return disciplineColumn;
    }

    public void setDisciplineColumn(Integer disciplineColumn) {
        this.disciplineColumn = disciplineColumn;
    }

    public Integer getBudgetManHourColumn() {
        return budgetManHourColumn;
    }

    public void setBudgetManHourColumn(Integer budgetManHourColumn) {
        this.budgetManHourColumn = budgetManHourColumn;
    }

    public Integer getAreaName() {
        return areaName;
    }

    public void setAreaName(Integer areaName) {
        this.areaName = areaName;
    }

    public Integer getModuleName() {
        return moduleName;
    }

    public void setModuleName(Integer moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getIsSubDrawing() {
        return isSubDrawing;
    }

    public void setIsSubDrawing(Integer isSubDrawing) {
        this.isSubDrawing = isSubDrawing;
    }

    public Integer getDrawingCategory() {
        return drawingCategory;
    }

    public void setDrawingCategory(Integer drawingCategory) {
        this.drawingCategory = drawingCategory;
    }

    public Integer getWorkNet() {
        return workNet;
    }

    public void setWorkNet(Integer workNet) {
        this.workNet = workNet;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public Integer getBlock() {
        return block;
    }

    public void setBlock(Integer block) {
        this.block = block;
    }

    public Integer getSmallArea() {
        return smallArea;
    }

    public void setSmallArea(Integer smallArea) {
        this.smallArea = smallArea;
    }

    public Integer getDesignDiscipline() {
        return designDiscipline;
    }

    public void setDesignDiscipline(Integer designDiscipline) {
        this.designDiscipline = designDiscipline;
    }

    public Integer getInstallationDrawingNo() {
        return installationDrawingNo;
    }

    public void setInstallationDrawingNo(Integer installationDrawingNo) {
        this.installationDrawingNo = installationDrawingNo;
    }

    public Integer getAmendmentNo() {
        return amendmentNo;
    }

    public void setAmendmentNo(Integer amendmentNo) {
        this.amendmentNo = amendmentNo;
    }

    public Integer getOriginatorNameColumn() {
        return originatorNameColumn;
    }

    public void setOriginatorNameColumn(Integer originatorNameColumn) {
        this.originatorNameColumn = originatorNameColumn;
    }

    public Integer getOrgCodeColumn() {
        return orgCodeColumn;
    }

    public void setOrgCodeColumn(Integer orgCodeColumn) {
        this.orgCodeColumn = orgCodeColumn;
    }

    public Integer getSystemCodeColumn() {
        return systemCodeColumn;
    }

    public void setSystemCodeColumn(Integer systemCodeColumn) {
        this.systemCodeColumn = systemCodeColumn;
    }

    public Integer getDiscCodeColumn() {
        return discCodeColumn;
    }

    public void setDiscCodeColumn(Integer discCodeColumn) {
        this.discCodeColumn = discCodeColumn;
    }

    public Integer getDocTypeColumn() {
        return docTypeColumn;
    }

    public void setDocTypeColumn(Integer docTypeColumn) {
        this.docTypeColumn = docTypeColumn;
    }

    public Integer getShortCodeColumn() {
        return shortCodeColumn;
    }

    public void setShortCodeColumn(Integer shortCodeColumn) {
        this.shortCodeColumn = shortCodeColumn;
    }

    public Integer getSheetNoColumn() {
        return sheetNoColumn;
    }

    public void setSheetNoColumn(Integer sheetNoColumn) {
        this.sheetNoColumn = sheetNoColumn;
    }

    public Integer getChainCodeColumnColumn() {
        return chainCodeColumnColumn;
    }

    public void setChainCodeColumnColumn(Integer chainCodeColumnColumn) {
        this.chainCodeColumnColumn = chainCodeColumnColumn;
    }

    public Integer getForEniSubmissionColumn() {
        return forEniSubmissionColumn;
    }

    public void setForEniSubmissionColumn(Integer forEniSubmissionColumn) {
        this.forEniSubmissionColumn = forEniSubmissionColumn;
    }

    public Integer getClientDocRevColumn() {
        return clientDocRevColumn;
    }

    public void setClientDocRevColumn(Integer clientDocRevColumn) {
        this.clientDocRevColumn = clientDocRevColumn;
    }

    public Integer getClientDocNoColumn() {
        return clientDocNoColumn;
    }

    public void setClientDocNoColumn(Integer clientDocNoColumn) {
        this.clientDocNoColumn = clientDocNoColumn;
    }

    public Integer getValidityStatusColumn() {
        return validityStatusColumn;
    }

    public void setValidityStatusColumn(Integer validityStatusColumn) {
        this.validityStatusColumn = validityStatusColumn;
    }

    public Integer getSurveillanceTypeColumn() {
        return surveillanceTypeColumn;
    }

    public void setSurveillanceTypeColumn(Integer surveillanceTypeColumn) {
        this.surveillanceTypeColumn = surveillanceTypeColumn;
    }

    public Integer getIdcCheckColumn() {
        return idcCheckColumn;
    }

    public void setIdcCheckColumn(Integer idcCheckColumn) {
        this.idcCheckColumn = idcCheckColumn;
    }

    public Integer getIdcSponsorColumn() {
        return idcSponsorColumn;
    }

    public void setIdcSponsorColumn(Integer idcSponsorColumn) {
        this.idcSponsorColumn = idcSponsorColumn;
    }

    public Integer getIdcDateColumn() {
        return idcDateColumn;
    }

    public void setIdcDateColumn(Integer idcDateColumn) {
        this.idcDateColumn = idcDateColumn;
    }

    public Integer getPlanStartDateColumn() {
        return planStartDateColumn;
    }

    public void setPlanStartDateColumn(Integer planStartDateColumn) {
        this.planStartDateColumn = planStartDateColumn;
    }

    public Integer getPlanIFRDateColumn() {
        return planIFRDateColumn;
    }

    public void setPlanIFRDateColumn(Integer planIFRDateColumn) {
        this.planIFRDateColumn = planIFRDateColumn;
    }

    public Integer getPlanIFDDateColumn() {
        return planIFDDateColumn;
    }

    public void setPlanIFDDateColumn(Integer planIFDDateColumn) {
        this.planIFDDateColumn = planIFDDateColumn;
    }

    public Integer getPlanIFCDateColumn() {
        return planIFCDateColumn;
    }

    public void setPlanIFCDateColumn(Integer planIFCDateColumn) {
        this.planIFCDateColumn = planIFCDateColumn;
    }

    public Integer getPlanIFIDateColumn() {
        return planIFIDateColumn;
    }

    public void setPlanIFIDateColumn(Integer planIFIDateColumn) {
        this.planIFIDateColumn = planIFIDateColumn;
    }

    public Integer getPlanIABDateColumn() {
        return planIABDateColumn;
    }

    public void setPlanIABDateColumn(Integer planIABDateColumn) {
        this.planIABDateColumn = planIABDateColumn;
    }

    public Integer getForecastStartDateColumn() {
        return forecastStartDateColumn;
    }

    public void setForecastStartDateColumn(Integer forecastStartDateColumn) {
        this.forecastStartDateColumn = forecastStartDateColumn;
    }

    public Integer getForecastIFRDateColumn() {
        return forecastIFRDateColumn;
    }

    public void setForecastIFRDateColumn(Integer forecastIFRDateColumn) {
        this.forecastIFRDateColumn = forecastIFRDateColumn;
    }

    public Integer getForecastIFDDateColumn() {
        return forecastIFDDateColumn;
    }

    public void setForecastIFDDateColumn(Integer forecastIFDDateColumn) {
        this.forecastIFDDateColumn = forecastIFDDateColumn;
    }

    public Integer getForecastIFCDateColumn() {
        return forecastIFCDateColumn;
    }

    public void setForecastIFCDateColumn(Integer forecastIFCDateColumn) {
        this.forecastIFCDateColumn = forecastIFCDateColumn;
    }

    public Integer getForecastIFIDateColumn() {
        return forecastIFIDateColumn;
    }

    public void setForecastIFIDateColumn(Integer forecastIFIDateColumn) {
        this.forecastIFIDateColumn = forecastIFIDateColumn;
    }

    public Integer getForecastIABDateColumn() {
        return forecastIABDateColumn;
    }

    public void setForecastIABDateColumn(Integer forecastIABDateColumn) {
        this.forecastIABDateColumn = forecastIABDateColumn;
    }

    public Integer getDeleteFlagColumn() {
        return deleteFlagColumn;
    }

    public void setDeleteFlagColumn(Integer deleteFlagColumn) {
        this.deleteFlagColumn = deleteFlagColumn;
    }

    public Integer getDcrNoColumn() {
        return dcrNoColumn;
    }

    public void setDcrNoColumn(Integer dcrNoColumn) {
        this.dcrNoColumn = dcrNoColumn;
    }

    public Integer getDcrStatusColumn() {
        return dcrStatusColumn;
    }

    public void setDcrStatusColumn(Integer dcrStatusColumn) {
        this.dcrStatusColumn = dcrStatusColumn;
    }

    public Integer getDcrReplyDateColumn() {
        return dcrReplyDateColumn;
    }

    public void setDcrReplyDateColumn(Integer dcrReplyDateColumn) {
        this.dcrReplyDateColumn = dcrReplyDateColumn;
    }

    public Integer getDcrSubmissionDateColumn() {
        return dcrSubmissionDateColumn;
    }

    public void setDcrSubmissionDateColumn(Integer dcrSubmissionDateColumn) {
        this.dcrSubmissionDateColumn = dcrSubmissionDateColumn;
    }

    public Integer getProgressStageColumn() {
        return progressStageColumn;
    }

    public void setProgressStageColumn(Integer progressStageColumn) {
        this.progressStageColumn = progressStageColumn;
    }

    public Integer getRevNoColumn() {
        return revNoColumn;
    }

    public void setRevNoColumn(Integer revNoColumn) {
        this.revNoColumn = revNoColumn;
    }

    public Integer getUploadDateColumn() {
        return uploadDateColumn;
    }

    public void setUploadDateColumn(Integer uploadDateColumn) {
        this.uploadDateColumn = uploadDateColumn;
    }

    public Integer getOutgoingTransmittalColumn() {
        return outgoingTransmittalColumn;
    }

    public void setOutgoingTransmittalColumn(Integer outgoingTransmittalColumn) {
        this.outgoingTransmittalColumn = outgoingTransmittalColumn;
    }

    public Integer getIncomingTransmittalColumn() {
        return incomingTransmittalColumn;
    }

    public void setIncomingTransmittalColumn(Integer incomingTransmittalColumn) {
        this.incomingTransmittalColumn = incomingTransmittalColumn;
    }

    public Integer getReplyDateColumn() {
        return replyDateColumn;
    }

    public void setReplyDateColumn(Integer replyDateColumn) {
        this.replyDateColumn = replyDateColumn;
    }

    public Integer getReplyStatusColumn() {
        return replyStatusColumn;
    }

    public void setReplyStatusColumn(Integer replyStatusColumn) {
        this.replyStatusColumn = replyStatusColumn;
    }

    public Integer getDcrOutgoingDateColumn() {
        return dcrOutgoingDateColumn;
    }

    public void setDcrOutgoingDateColumn(Integer dcrOutgoingDateColumn) {
        this.dcrOutgoingDateColumn = dcrOutgoingDateColumn;
    }

    public Integer getDcrRequestColumn() {
        return dcrRequestColumn;
    }

    public void setDcrRequestColumn(Integer dcrRequestColumn) {
        this.dcrRequestColumn = dcrRequestColumn;
    }
}
