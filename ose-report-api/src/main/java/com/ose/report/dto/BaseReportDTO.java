package com.ose.report.dto;

import com.ose.dto.BaseDTO;
import com.ose.report.vo.ReportExportType;
import com.ose.vo.InspectParty;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class BaseReportDTO extends BaseDTO {

    private static final long serialVersionUID = 19506801711784147L;

    private static SimpleDateFormat REPORT_NO_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    private Long orgId = null;

    private Long projectId = null;

    private String reportName = "";

    private String reportQrCode;

    private String reportNoPrefix = "";

    private String reportNo = "";

    private Long createdBy = 0L;

    private String templateNo = "";

    private String serialNo = "";

    private String clientLogoDir;

    private String contractorLogoDir;

    private String projectNamePrefix;

    private String rev;

    private String supplier;

    private String location;

    private String structureName;

    private String drawingNo;

    private Date date;

    private String tagNo;

    private String materialType;

    private String weldingProcess;

    private String material;

    private String moduleNo;

    private List<InspectParty> inspectParties;

    private ReportExportType exportType = ReportExportType.PDF;

    public BaseReportDTO() {
        this("", "");
    }

    public BaseReportDTO(String reportName, String reportNoPrefix) {
        setReportName(reportName);
        setReportNoPrefix(reportNoPrefix);
//        generateReportNo();
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

    public String getReportQrCode() {
        return reportQrCode;
    }

    public void setReportQrCode(String reportQrCode) {
        this.reportQrCode = reportQrCode;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportNoPrefix() {
        return reportNoPrefix;
    }

    public void setReportNoPrefix(String reportNoPrefix) {
        this.reportNoPrefix = reportNoPrefix;
    }

    public static SimpleDateFormat getReportNoFormat() {
        return REPORT_NO_FORMAT;
    }

    public static void setReportNoFormat(SimpleDateFormat reportNoFormat) {
        REPORT_NO_FORMAT = reportNoFormat;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public List<InspectParty> getInspectParties() {
        return inspectParties;
    }

    public void setInspectParties(List<InspectParty> inspectParties) {
        this.inspectParties = inspectParties;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ReportExportType getExportType() {
        return exportType;
    }

    public void setExportType(ReportExportType exportType) {
        this.exportType = exportType;
    }

    public String getTemplateNo() {
        return templateNo;
    }

    public void setTemplateNo(String templateNo) {
        this.templateNo = templateNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getProjectNamePrefix() {
        return projectNamePrefix;
    }

    public void setProjectNamePrefix(String projectNamePrefix) {
        this.projectNamePrefix = projectNamePrefix;
    }

    public String getClientLogoDir() {
        return clientLogoDir;
    }

    public void setClientLogoDir(String clientLogoDir) {
        this.clientLogoDir = clientLogoDir;
    }

    public String getContractorLogoDir() {
        return contractorLogoDir;
    }

    public void setContractorLogoDir(String contractorLogoDir) {
        this.contractorLogoDir = contractorLogoDir;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStructureName() {
        return structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTagNo() {
        return tagNo;
    }

    public void setTagNo(String tagNo) {
        this.tagNo = tagNo;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public String getWeldingProcess() {
        return weldingProcess;
    }

    public void setWeldingProcess(String weldingProcess) {
        this.weldingProcess = weldingProcess;
    }
}
