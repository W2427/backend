package com.ose.tasks.entity.bpm;

import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 外检报告类型与模版对应关系类。
 */
@Entity
@Table(name = "external_inspection_report_template_config",
    indexes = {
        @Index(name = "template_index_0", columnList = "projectId, reportName")}
)
public class BpmExInspReportTemplateConfig extends BaseBizEntity {
    /**
     *
     */
    private static final long serialVersionUID = -851269369929838606L;

    @Schema(description = "项目ID")
    @Column(length = 16)
    private Long projectId;

    @Schema(description = "报表名")
    @Column
    private String reportName;

    @Schema(description = "模版名")
    @Column
    private String templateName;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
