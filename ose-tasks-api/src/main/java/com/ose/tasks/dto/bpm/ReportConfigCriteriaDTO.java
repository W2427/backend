package com.ose.tasks.dto.bpm;

import com.ose.dto.PageDTO;
import com.ose.tasks.vo.qc.ReportSubType;
import com.ose.tasks.vo.qc.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 *  报告配置列表查询DTO
 */
public class ReportConfigCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -8274385633834500231L;

    @Schema(description = "报告名称")
    @Column
    private String reportName;

    @Schema(description = "生成报告使用的生成类")
    @Column
    private String reportGenerateClass;

    @Schema(description = "报告编号")
    @Column
    private String reportCode;

    //使用 ReportSubType 的枚举作为选择接口
    @Schema(description = "报告子类型")
    @Column
    @Enumerated(EnumType.STRING)
    private ReportSubType reportSubType;


    //使用 ReportType 的枚举作为选择接口
    @Schema(description = "报告子类型")
    @Column
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Schema(description = "工序 ID")
    @Column
    private Long processId;

    @Schema(description = "是否是 封面")
    @Column(columnDefinition = "bit default 0")
    private Boolean isCover;

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
}
