package com.ose.tasks.entity.bpm;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.ose.tasks.vo.bpm.MailRunningStatus;
import com.ose.util.StringUtils;

/**
 * 外检邮件申请。
 */
@Entity
@Table(name = "external_inspection_mail_history")
public class BpmExInspMailHistory extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    private Long orgId;

    private Long projectId;

    private Date sendTime;

    private String toMail;

    private String ccMail;

    private String subject;

    private String mainContent;

    // 目录
    @Column(columnDefinition = "text")
    private String catalogue;

    // 报表
    @Column(columnDefinition = "text")
    private String reports;

    // 附件
    @Column(columnDefinition = "text")
    private String attachments;

    private Long operator;

    private String serverUrl;

    @Enumerated(EnumType.STRING)
    private MailRunningStatus sendStatus;

    private String memo;

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    @JsonProperty(value = "operator", access = READ_ONLY)
    public ReferenceData getOperatorRef() {
        return this.operator == null
            ? null
            : new ReferenceData(this.operator);
    }

    @Override
    public Set<Long> relatedUserIDs() {

        Set<Long> userIDs = new HashSet<>();

        if (this.getOperator() != null && this.getOperator() != 0L) {
            userIDs.add(this.getOperator());
        }

        return userIDs;
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
            return new ArrayList<ActReportDTO>();
        }
    }

    @JsonIgnore
    public void setJsonAttachments(List<ActReportDTO> attachments) {
        if (attachments != null) {
            this.attachments = StringUtils.toJSON(attachments);
        }
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(String catalogue) {
        this.catalogue = catalogue;
    }

    @JsonProperty(value = "catalogue", access = JsonProperty.Access.READ_ONLY)
    public List<ActReportDTO> getJsonCatalogue() {
        if (catalogue != null && !"".equals(catalogue)) {
            return StringUtils.decode(catalogue, new TypeReference<List<ActReportDTO>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonCatalogue(List<ActReportDTO> catalogue) {
        if (catalogue != null) {
            this.catalogue = StringUtils.toJSON(catalogue);
        }
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public MailRunningStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(MailRunningStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}

