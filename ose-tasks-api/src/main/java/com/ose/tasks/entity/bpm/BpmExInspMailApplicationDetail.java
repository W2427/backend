package com.ose.tasks.entity.bpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.util.StringUtils;
import com.ose.vo.InspectParty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "bpm_external_inspection_mail_application_detail")
public class BpmExInspMailApplicationDetail extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "附件")
    @Column(columnDefinition = "text")
    private String attachments;

    // 外检时间
    @Column(name = "external_inspection_time")
    private Date externalInspectionTime;

    // 报检人
    private Long operator;

    // 备注
    private String comment;

    private Long scheduleId;

    private Long externallInspectionMailApplicationId;

    // 附件
    @Column(columnDefinition = "text")
    private String reports;

    // 附件
    @Column(columnDefinition = "text")
    private String seriesNos;

    // 状态
    @Enumerated(EnumType.STRING)
    private InspectParty inspectParty;

    public Date getExternalInspectionTime() {
        return externalInspectionTime;
    }

    public void setExternalInspectionTime(Date externalInspectionTime) {
        this.externalInspectionTime = externalInspectionTime;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operatorId) {
        this.operator = operator;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getReports() {
        return reports;
    }

    public void setReports(String reports) {
        this.reports = reports;
    }

    @JsonProperty(value = "reports", access = JsonProperty.Access.READ_ONLY)
    public List<ActReportDTO> getJsonReports() {
        if (reports != null && !"".equals(reports)) {
            return StringUtils.decode(reports, new TypeReference<List<ActReportDTO>>() {
            });
        } else {
            return new ArrayList<ActReportDTO>();
        }
    }

    @JsonIgnore
    public void setJsonReports(List<ActReportDTO> reports) {
        if (reports != null) {
            this.reports = StringUtils.toJSON(reports);
        }
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    @JsonProperty(value = "attachments", access = JsonProperty.Access.READ_ONLY)
    public List<ActReportDTO> getJsonAttachments() {
        if (attachments != null && !"".equals(attachments)) {
            return StringUtils.decode(attachments, new TypeReference<List<ActReportDTO>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonAttachments(List<ActReportDTO> attachments) {
        if (attachments != null) {
            this.attachments = StringUtils.toJSON(attachments);
        }
    }

    public String getSeriesNos() {
        return seriesNos;
    }

    public void setSeriesNos(String seriesNos) {
        this.seriesNos = seriesNos;
    }

    @JsonProperty(value = "seriesNos", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonSeriesNos() {
        if (seriesNos != null && !"".equals(seriesNos)) {
            return StringUtils.decode(seriesNos, new TypeReference<List<String>>() {
            });
        } else {
            return null;
        }
    }

    @JsonIgnore
    public void setJsonSeriesNos(List<String> seriesNos) {
        if (seriesNos != null) {
            this.seriesNos = StringUtils.toJSON(seriesNos);
        }
    }

    public InspectParty getInspectParty() {
        return inspectParty;
    }

    public void setInspectParty(InspectParty inspectParty) {
        this.inspectParty = inspectParty;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getExternallInspectionMailApplicationId() {
        return externallInspectionMailApplicationId;
    }

    public void setExternallInspectionMailApplicationId(Long externallInspectionMailApplicationId) {
        this.externallInspectionMailApplicationId = externallInspectionMailApplicationId;
    }

}
