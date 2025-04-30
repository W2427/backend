package com.ose.tasks.entity.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.vo.qc.InspectType;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.tasks.vo.qc.ReportSubType;
import com.ose.tasks.vo.qc.ReportType;
import com.ose.util.StringUtils;
import com.ose.vo.InspectResult;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * qc检验报告实体
 */
@Entity
@Table(name = "qc_report",
indexes = {
    @Index(columnList = "projectId,seriesNo"),
    @Index(columnList = "projectId,qrcode"),
    @Index(columnList = "qrcode,reportStatus"),
    @Index(columnList = "projectId,reportSubType,moduleName,reportStatus"),
    @Index(columnList = "scheduleId,reportStatus"),
    @Index(columnList = "projectId,reportNo"),
})
public class QCReport extends BaseBizEntity {

    private static final long serialVersionUID = -8635813266138087045L;

    @Schema(description = "ORG ID")
    @Column
    private Long orgId;

    @Schema(description = "PROJECT ID")
    @Column
    private Long projectId;

    @Schema(description = "报告编号")
    @Column
    private String reportNo;

    @Schema(description = "模块号")
    @Column
    private String moduleName;

    @Schema(description = "xls 报告 ID")
    @Column
    private Long excelReportFileId;

    @Schema(description = "xls 报告路径")
    @Column
    private String excelReportFilePath;

    @Schema(description = "xls 报告名")
    @Column
    private String excelReportFileName;

    @Schema(description = "PDF 报告ID")
    @Column
    private Long pdfReportFileId;

    @Schema(description = "PDF 报告路径")
    @Column
    private String pdfReportFilePath;

    @Schema(description = "PDF 报告名")
    @Column
    private String pdfReportFileName;

    @Schema(description = "是否是封面")
    private Boolean isCover;

    @Schema(description = "流程实例Id")
    @Column(columnDefinition = "text")
    private String actInstIds;

    @Schema(description = "报告二维码")
    @Column
    private String qrcode;

    @Schema(description = "报告对应的工序")
    @Column
    private String process;

    @Schema(description = "工序阶段")
    @Column
    private String processStage;

    @Schema(description = "报告流水号")
    @Column
    private Integer seriesNum;

    @Schema(description = "实体编号列表")
    @Column(columnDefinition = "text")
    private String entityNos;

    @Schema(description = "tag实体Id")
    @Column
    private Long parentEntityId;

    @Schema(description = "检验报告子类型，当前用于区分报告子类型 管道入库，阀门入库等")
    @Column
    @Enumerated(EnumType.STRING)
    private ReportSubType reportSubType;

    @Schema(description = "检验方")
    @Column
    private String inspectParties;

    @Schema(description = "生成的报告类型，初始外检报告；重新合成外检报告；非报检报告")
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Schema(description = "检验类型 外检；内检；非检验")
    @Enumerated(EnumType.STRING)
    private InspectType inspectType;

    @Schema(description = "检验申请的ID")
    @Column
    private Long scheduleId;

    @Schema(description = "报告编号")
    @Column
    private String seriesNo;

    @Schema(description = "NDT类型")
    @Column
    private String ndeType;

    @Schema(description = "报告状态 INIT(0, 待回传),DONE(1,已回传), CANCEL(2,被废止),PENDING(3,需补传);")
    @Column
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus = ReportStatus.INIT;

    @Schema(description = "是否是一次性处理报告")
    @Column
    private Boolean isOneOffReport = false;

    @Schema(description = "操作者")
    @Column
    private Long operator;

    @Schema(description = "操作者姓名")
    @Column
    private String operatorName;

    @Schema(description = "上传文件的ID")
    @Column
    private Long uploadFileId;

    @Schema(description = "上传文件的Path")
    @Column
    private String uploadFilePath;

    @Schema(description = "PDF报告文件页数")
    @Column
    private int pdfReportPageCount = 0;

    @Schema(description = "上传文件页数")
    @Column
    private int uploadReportPageCount = 0;

    @Schema(description = "二次上传文件ID")
    @Column
    private Long secondUploadFileId;

    @Schema(description = "原始报告，对于B类报告（再处理报告），原始报告的ID")
    @Column
    private Long parentReportId;

    @Schema(description = "是否是二次上传，报告后期补传")
    @Column
    private Boolean isSecondUpload;

    @Schema(description = "备注")
    @Column(columnDefinition = "text")
    private String memo;

    @Schema(description = "外检报告的检验结果， A B C")
    @Column
    @Enumerated(EnumType.STRING)
    private InspectResult inspectResult;

    @Transient
    private ActReportDTO actReport;

    @Schema(description = "生成报告的报错信息")
    @Transient
    private String reportGenerateError;

    @Schema(description = "批次报告处理完成")
    @Column(columnDefinition = "BIT DEFAULT 0")
    private Boolean isReportBatchConfirmed = true;

    @Schema(description = "外检申请时间")
    @Column
    private Date applyDate;

    @Schema(description = "打包 报告ID")
    @Column
    private Long packageReportFileId;

    @Schema(description = "打包 报告path")
    @Column
    private String packageReportFilePath;

    @Schema(description = "打包 报告name")
    @Column
    private String packageReportFileName;

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

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getProcessStage() {
        return processStage;
    }

    public void setProcessStage(String processStage) {
        this.processStage = processStage;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public InspectType getInspectType() {
        return inspectType;
    }

    public void setInspectType(InspectType inspectType) {
        this.inspectType = inspectType;
    }

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public Boolean getOneOffReport() {
        return isOneOffReport;
    }

    public void setOneOffReport(Boolean oneOffReport) {
        isOneOffReport = oneOffReport;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public Long getUploadFileId() {
        return uploadFileId;
    }

    public void setUploadFileId(Long uploadFileId) {
        this.uploadFileId = uploadFileId;
    }

    public Long getExcelReportFileId() {
        return excelReportFileId;
    }

    public void setExcelReportFileId(Long excelReportFileId) {
        this.excelReportFileId = excelReportFileId;
    }

    public Long getPdfReportFileId() {
        return pdfReportFileId;
    }

    public void setPdfReportFileId(Long pdfReportFileId) {
        this.pdfReportFileId = pdfReportFileId;
    }

    public String getNdeType() {
        return ndeType;
    }

    public void setNdeType(String ndeType) {
        this.ndeType = ndeType;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Boolean getCover() {
        return isCover;
    }

    public void setCover(Boolean cover) {
        isCover = cover;
    }

    public int getPdfReportPageCount() {
        return pdfReportPageCount;
    }

    public void setPdfReportPageCount(int pdfReportPageCount) {
        this.pdfReportPageCount = pdfReportPageCount;
    }

    public int getUploadReportPageCount() {
        return uploadReportPageCount;
    }

    public void setUploadReportPageCount(int uploadReportPageCount) {
        this.uploadReportPageCount = uploadReportPageCount;
    }

    public String getInspectParties() {
        return inspectParties;
    }

    public void setInspectParties(String inspectParties) {
        this.inspectParties = inspectParties;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public Long getSecondUploadFileId() {
        return secondUploadFileId;
    }

    public void setSecondUploadFileId(Long secondUploadFileId) {
        this.secondUploadFileId = secondUploadFileId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getSeriesNum() {
        return seriesNum;
    }

    public void setSeriesNum(Integer seriesNum) {
        this.seriesNum = seriesNum;
    }

    public ReportSubType getReportSubType() {
        return reportSubType;
    }

    public void setReportSubType(ReportSubType reportSubType) {
        this.reportSubType = reportSubType;
    }

    public Long getParentReportId() {
        return parentReportId;
    }

    public void setParentReportId(Long parentReportId) {
        this.parentReportId = parentReportId;
    }

    public Boolean getSecondUpload() {
        return isSecondUpload;
    }

    public void setSecondUpload(Boolean secondUpload) {
        isSecondUpload = secondUpload;
    }

    public InspectResult getInspectResult() {
        return inspectResult;
    }

    public void setInspectResult(InspectResult inspectResult) {
        this.inspectResult = inspectResult;
    }

    @JsonProperty(value = "inspectParties", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonInspectParties() {
        if (inspectParties != null && !"".equals(inspectParties)) {
            return StringUtils.decode(inspectParties, new TypeReference<List<String>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonInspectParties(List<String> inspectParties) {
        if (inspectParties != null) {
            this.inspectParties = StringUtils.toJSON(inspectParties);
        }
    }

    public ActReportDTO getActReport() {
        return actReport;
    }

    public void setActReport(ActReportDTO actReport) {
        this.actReport = actReport;
    }

    public String getReportGenerateError() {
        return reportGenerateError;
    }

    public void setReportGenerateError(String reportGenerateError) {
        this.reportGenerateError = reportGenerateError;
    }

    public Boolean getReportBatchConfirmed() {
        return isReportBatchConfirmed;
    }

    public void setReportBatchConfirmed(Boolean reportBatchConfirmed) {
        isReportBatchConfirmed = reportBatchConfirmed;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getEntityNos() {
        return entityNos;
    }

    public void setEntityNos(String entityNos) {
        this.entityNos = entityNos;
    }

    @JsonProperty(value = "entityNos", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonEntityNos() {
        if (entityNos != null && !"".equals(entityNos)) {
            return StringUtils.decode(entityNos, new TypeReference<List<String>>() {
            });
        } else {
            return new ArrayList<String>();
        }
    }

    @JsonIgnore
    public void setJsonEntityNos(List<String> entityNos) {
        if (entityNos != null) {
            this.entityNos = StringUtils.toJSON(entityNos);
        }
    }

    public String getActInstIds() {
        return actInstIds;
    }

    public void setActInstIds(String actInstIds) {
        this.actInstIds = actInstIds;
    }

    @JsonProperty(value = "actInstIds", access = JsonProperty.Access.READ_ONLY)
    public List<Long> getJsonActInstIds() {
        if (actInstIds != null && !"".equals(actInstIds)) {
            return StringUtils.decode(actInstIds, new TypeReference<List<Long>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonActInstIds(List<Long> actInstIds) {
        if (actInstIds != null) {
            this.actInstIds = StringUtils.toJSON(actInstIds);
        }
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getExcelReportFilePath() {
        return excelReportFilePath;
    }

    public void setExcelReportFilePath(String excelReportFilePath) {
        this.excelReportFilePath = excelReportFilePath;
    }

    public String getExcelReportFileName() {
        return excelReportFileName;
    }

    public void setExcelReportFileName(String excelReportFileName) {
        this.excelReportFileName = excelReportFileName;
    }

    public String getPdfReportFilePath() {
        return pdfReportFilePath;
    }

    public void setPdfReportFilePath(String pdfReportFilePath) {
        this.pdfReportFilePath = pdfReportFilePath;
    }

    public String getPdfReportFileName() {
        return pdfReportFileName;
    }

    public void setPdfReportFileName(String pdfReportFileName) {
        this.pdfReportFileName = pdfReportFileName;
    }

    public Long getParentEntityId() {
        return parentEntityId;
    }

    public void setParentEntityId(Long parentEntityId) {
        this.parentEntityId = parentEntityId;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }

    public Long getPackageReportFileId() {
        return packageReportFileId;
    }

    public void setPackageReportFileId(Long packageReportFileId) {
        this.packageReportFileId = packageReportFileId;
    }

    public String getPackageReportFilePath() {
        return packageReportFilePath;
    }

    public void setPackageReportFilePath(String packageReportFilePath) {
        this.packageReportFilePath = packageReportFilePath;
    }

    public String getPackageReportFileName() {
        return packageReportFileName;
    }

    public void setPackageReportFileName(String packageReportFileName) {
        this.packageReportFileName = packageReportFileName;
    }
}
