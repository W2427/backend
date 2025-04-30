package com.ose.tasks.entity.bpm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 上传外检报告后确认项 实体类。
 */
@Entity
@Table(name = "bpm_external_inspection_confirm",
    indexes = {
        @Index(columnList = "uploadHistoryId"),
        @Index(columnList = "orgId,projectId,qrcode"),
        @Index(columnList = "orgId,seriesNo")
    })
public class BpmExInspConfirm extends BaseBizEntity {

    private static final long serialVersionUID = -4387445149304502246L;

    @Schema(description = "ORG ID")
    @Column
    private Long orgId;

    @Schema(description = "PROJECT ID")
    @Column
    private Long projectId;

    @Schema(description = "上传历史ID")
    @Column
    private Long uploadHistoryId;

    @Schema(description = "报检人")
    @Column
    private Long operator;

    @Schema(description = "报告二维码")
    @Column
    private String qrcode;

    @Schema(description = "报告号")
    @Column
    private String reportNo;

    @Schema(description = "流水号")
    @Column
    private String seriesNo;

    @Schema(description = "上传页数")
    @Column
    private int pageCount;

    @Schema(description = "原始报告页数")
    @Column
    private Integer initPageCount;

    @Schema(description = "是否是二次上传")
    @Column
    private Boolean IsSecondUpload = false;

    @Schema(description = "是否已上传确认过")
    @Column
    private Boolean isUploaded = false;

    @Schema(description = "上传文件 ID")
    @Column
    private String uploadFileId;

    @Schema(description = "报告")
    @Column(columnDefinition = "text")
    private String report;

    @Schema(description = "report_gateway 报告处理网关 A/ B/C")
    @Column
    private String reportGateway;

    @JsonProperty(value = "report", access = JsonProperty.Access.READ_ONLY)
    public ActReportDTO getJsonReportReadOnly() {
        if (report != null && !"".equals(report)) {
            return StringUtils.decode(report, new TypeReference<ActReportDTO>() {
            });
        } else {
            return new ActReportDTO();
        }
    }

    @JsonIgnore
    public void setJsonReport(ActReportDTO report) {
        if (report != null) {
            this.report = StringUtils.toJSON(report);
        }
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

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getInitPageCount() {
        return initPageCount;
    }

    public void setInitPageCount(Integer initPageCount) {
        this.initPageCount = initPageCount;
    }

    public Boolean getSecondUpload() {
        return IsSecondUpload;
    }

    public void setSecondUpload(Boolean secondUpload) {
        IsSecondUpload = secondUpload;
    }

    public Boolean getUploaded() {
        return isUploaded;
    }

    public void setUploaded(Boolean uploaded) {
        isUploaded = uploaded;
    }

    public String getReportGateway() {
        return reportGateway;
    }

    public void setReportGateway(String reportGateway) {
        this.reportGateway = reportGateway;
    }

    public Long getUploadHistoryId() {
        return uploadHistoryId;
    }

    public void setUploadHistoryId(Long uploadHistoryId) {
        this.uploadHistoryId = uploadHistoryId;
    }

    public String getUploadFileId() {
        return uploadFileId;
    }

    public void setUploadFileId(String uploadFileId) {
        this.uploadFileId = uploadFileId;
    }
}
