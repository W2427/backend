package com.ose.tasks.entity.report;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.qc.ReportType;
import com.ose.tasks.vo.qc.ReportSubType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 报告设置 实体类。
 */
@Entity
@Table(name = "report_config")
public class ReportConfig extends BaseBizEntity {


    private static final long serialVersionUID = -9075758687880846090L;

    @Schema(description = "ORG ID")
    @Column
    private Long orgId;

    @Schema(description = "PROJECT ID")
    @Column
    private Long projectId;

    @Schema(description = "报告名称")
    @Column
    private String reportName;

    @Schema(description = "生成报告使用的生成类")
    @Column
    private String reportGenerateClass;

    @Schema(description = "报告编号")
    @Column
    private String reportCode;

    //使用 ReportType 的枚举作为选择接口
    @Schema(description = "报告类型")
    @Column
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    //使用 ReportSubType 的枚举作为选择接口
    @Schema(description = "报告子类型")
    @Column
    @Enumerated(EnumType.STRING)
    private ReportSubType reportSubType;

    @Schema(description = "工序 ID")
    @Column
    private Long processId;

    @Schema(description = "是否是 封面")
    @Column(columnDefinition = "bit default 0")
    private Boolean isCover;

    @Schema(description = "模版文件名")
    @Column
    private String templateName;

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
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

    public String getReportGenerateClass() {
        return reportGenerateClass;
    }

    public void setReportGenerateClass(String reportGenerateClass) {
        this.reportGenerateClass = reportGenerateClass;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
