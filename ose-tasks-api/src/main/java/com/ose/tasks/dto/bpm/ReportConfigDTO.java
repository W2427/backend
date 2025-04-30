package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.qc.ReportSubType;
import com.ose.tasks.vo.qc.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


public class ReportConfigDTO  extends BaseDTO {

    private static final long serialVersionUID = 3430964663110900369L;

    @Schema(description = "报告名称")
    private String reportName;

    @Schema(description = "生成报告使用的生成类")
    private String reportGenerateClass;

    @Schema(description = "报告编号")
    private String reportCode;

    //使用 ReportSubType 的枚举作为选择接口
    @Schema(description = "报告子类型")
    @Enumerated(EnumType.STRING)
    private ReportSubType reportSubType;


    //使用 ReportType 的枚举作为选择接口
    @Schema(description = "报告子类型")
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Schema(description = "工序 ID")
    private Long processId;

    @Schema(description = "是否是 封面")
    private Boolean isCover;

    @Schema(description = "模版文件名")
    private String templateName;

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportGenerateClass() {
        return reportGenerateClass;
    }

    public void setReportGenerateClass(String reportGenerateClass) {
        this.reportGenerateClass = reportGenerateClass;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public ReportSubType getReportSubType() {
        return reportSubType;
    }

    public void setReportSubType(ReportSubType reportSubType) {
        this.reportSubType = reportSubType;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Boolean getCover() {
        return isCover;
    }

    public void setCover(Boolean cover) {
        isCover = cover;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
