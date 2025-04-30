package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.bpm.ActInstDocType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


public class ExInspDocDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = -6552351434988798482L;

    @Schema(description = "模块Id")
    private Long moduleId;

    @Schema(description = "模块号")
    private String moduleNo;

    @Schema(description = "工序中文名称")
    private String processNameCn;

    @Schema(description = "工序英文名称")
    private String processNameEn;

    @Schema(description = "工序ID")
    private Long processId;

    @Schema(description = "工序阶段中文名称")
    private String processStageNameCn;

    @Schema(description = "工序阶段英文名称")
    private String processStageNameEn;

    @Schema(description = "工序阶段ID")
    private Long processStageId;

    @Schema(description = "报告Id")
    private String reportQrCode;

    @Schema(description = "报告名称")
    private String reportName;

    @Schema(description = "文件Id")
    private Long fileId;

    @Schema(description = "报告NO")
    private String reportNo;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "文件类型")
    @Enumerated(EnumType.STRING)
    private ActInstDocType type;

    public String getReportQrCode() {
        return reportQrCode;
    }

    public void setReportQrCode(String reportId) {
        this.reportQrCode = reportQrCode;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ActInstDocType getType() {
        return type;
    }

    public void setType(ActInstDocType type) {
        this.type = type;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public String getProcessNameCn() {
        return processNameCn;
    }

    public void setProcessNameCn(String processNameCn) {
        this.processNameCn = processNameCn;
    }

    public String getProcessNameEn() {
        return processNameEn;
    }

    public void setProcessNameEn(String processNameEn) {
        this.processNameEn = processNameEn;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcessStageNameCn() {
        return processStageNameCn;
    }

    public void setProcessStageNameCn(String processStageNameCn) {
        this.processStageNameCn = processStageNameCn;
    }

    public String getProcessStageNameEn() {
        return processStageNameEn;
    }

    public void setProcessStageNameEn(String processStageNameEn) {
        this.processStageNameEn = processStageNameEn;
    }

    public Long getProcessStageId() {
        return processStageId;
    }

    public void setProcessStageId(Long processStageId) {
        this.processStageId = processStageId;
    }
}
