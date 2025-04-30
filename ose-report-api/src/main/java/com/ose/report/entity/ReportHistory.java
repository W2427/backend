package com.ose.report.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.dto.OperatorDTO;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.report.dto.BaseReportDTO;
import com.ose.util.BeanUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.Date;

/**
 * 报告历史实体类。
 */
@Entity
@Table(name = "report_history")
public class ReportHistory extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 377688955338188538L;

    @Schema(description = "组织 ID")
    @Column(nullable = false)
    private Long orgId;

    // 项目 ID
    @Column(nullable = false)
    private Long projectId;

    // 模板文件名称
    @Column(nullable = false)
    private String templateName;

    // 报表名称
    @Column(nullable = false)
    private String reportName;

    // 报表编号
    @Column
    private String reportNo;

    // 报表编号
    @Column(name = "report_id")
    private String reportQrCode;

    // 报表数据
    @Column(length = 16777216)
    @JsonIgnore
    private String data;

    // 报表数据类型
    @Column
    private String dataType;

    // 文件 ID
    @Column(length = 16)
    private Long fileId;

    // 文件路径
    @Column
    private String filePath;

    @Transient
    private String fileType;//XLS/PDF/DOC/HTM

    @Schema(description = "生成报告的报错信息")
    @Transient
    private String generatedError;

    public ReportHistory() {
    }

    public ReportHistory(OperatorDTO operator, String templateName, BaseReportDTO reportDTO) {
        BeanUtils.copyProperties(reportDTO, this);
        this.setTemplateName(templateName);
        this.setData(StringUtils.toJSON(reportDTO));
        this.setDataType(reportDTO.getClass().toString());
        this.setCreatedAt(new Date());
        this.setCreatedBy(operator.getId());
        this.setStatus(EntityStatus.ACTIVE);
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

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getReportQrCode() {
        return reportQrCode;
    }

    public void setReportQrCode(String reportQrCode) {
        this.reportQrCode = reportQrCode;
    }

    public String getGeneratedError() {
        return generatedError;
    }

    public void setGeneratedError(String generatedError) {
        this.generatedError = generatedError;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
