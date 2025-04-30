package com.ose.tasks.entity.bpm;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.vo.bpm.ExInspScheduleCoordinateCategory;
import com.ose.tasks.vo.bpm.MailRunningStatus;
import com.ose.util.StringUtils;
import com.ose.vo.InspectParty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 外检邮件申请。
 */
@Entity
@Table(name = "bpm_external_inspection_mail_application")
public class BpmExInspMailApplication extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;


    @Schema(description = "ORG ID")
    @Column
    private Long orgId;

    @Schema(description = "PROJECT ID")
    @Column
    private Long projectId;

    @Schema(description = "to Mail")
    @Column
    private String toMail;

    @Schema(description = "cc Mail")
    @Column
    private String ccMail;

    @Schema(description = "from Mail")
    @Column
    private String fromMail;

    @Schema(description = "主题")
    @Column
    private String subject;

    @Schema(description = "邮件内容")
    @Column
    private String mainContent;

    @Schema(description = "目录报表")
    @Column(columnDefinition = "text")
    private String reports;

    @Schema(description = "附件")
    @Column(columnDefinition = "text")
    private String attachments;

    @Schema(description = "报检人")
    @Column
    private Long operator;

    @Schema(description = "文件详情")
    @Transient
    private List<BpmExInspMailApplicationDetail> details;

    @Schema(description = "临时文件名")
    @Transient
    private String temporaryFileName;

    @Schema(description = "备注")
    @Column
    private String comment;

    @Schema(description = "区分")
    @Column
    @Enumerated(EnumType.STRING)
    private ExInspScheduleCoordinateCategory coordinateCategory;

    @Schema(description = "类型")
    @Column
    @Enumerated(EnumType.STRING)
    private InspectParty inspectParty;

    @Schema(description = "邮件状态")
    @Column
    @Enumerated(EnumType.STRING)
    private MailRunningStatus mailStatus;

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ExInspScheduleCoordinateCategory getCoordinateCategory() {
        return coordinateCategory;
    }

    public void setCoordinateCategory(ExInspScheduleCoordinateCategory coordinateCategory) {
        this.coordinateCategory = coordinateCategory;
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

    public String getCcMail() {
        return ccMail;
    }

    public void setCcMail(String ccMail) {
        this.ccMail = ccMail;
    }

    public String getFromMail() {
        return fromMail;
    }

    public void setFromMail(String fromMail) {
        this.fromMail = fromMail;
    }

    public String getToMail() {
        return toMail;
    }

    public void setToMail(String toMail) {
        this.toMail = toMail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMainContent() {
        return mainContent;
    }

    public void setMainContent(String mainContent) {
        this.mainContent = mainContent;
    }

    public List<BpmExInspMailApplicationDetail> getDetails() {
        return details;
    }

    public InspectParty getInspectParty() {
        return inspectParty;
    }

    public void setInspectParty(InspectParty inspectParty) {
        this.inspectParty = inspectParty;
    }

    public MailRunningStatus getMailStatus() {
        return mailStatus;
    }

    public void setMailStatus(MailRunningStatus mailStatus) {
        this.mailStatus = mailStatus;
    }

    @JsonProperty(value = "details", access = JsonProperty.Access.READ_ONLY)
    public List<BpmExInspMailApplicationDetail> getDetailsReadOnly() {
        return this.details;
    }

    public void addDetails(BpmExInspMailApplicationDetail detail) {
        if (this.details == null) {
            this.details = new ArrayList<>();
        }
        this.details.add(detail);
    }

    public void setDetails(List<BpmExInspMailApplicationDetail> details) {
        this.details = details;
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

    public String getTemporaryFileName() {
        return temporaryFileName;
    }

    public void setTemporaryFileName(String temporaryFileName) {
        this.temporaryFileName = temporaryFileName;
    }
}

