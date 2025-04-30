package com.ose.tasks.entity.bpm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseEntity;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.qc.ReportSubTypeDTO;
import com.ose.util.StringUtils;
import com.ose.vo.InspectParty;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务 大字段表，存储大字段信息
 */
@Entity
@Table(name = "bpm_activity_instance_blob",
        indexes = {
            @Index(columnList = "baiId",unique = true),
            @Index(columnList = "projectId,id")

        })
public class BpmActivityInstanceBlob extends BaseEntity {

    private static final long serialVersionUID = 7739607618610101040L;

    public BpmActivityInstanceBlob(){

    }

    @JsonCreator
    public BpmActivityInstanceBlob(@JsonProperty("reportSubTypeInfo") List<ReportSubTypeDTO> reportSubTypeInfo,
                                    @JsonProperty("reports") List<ActReportDTO> reports,
                                    @JsonProperty("inspectParties") List<InspectParty> inspectParties) {

        this.reports = StringUtils.toJSON(reports);
        this.reportSubTypeInfo = StringUtils.toJSON(reportSubTypeInfo);
        this.inspectParties = StringUtils.toJSON(inspectParties);

    }

    //任务基础表id
    @Column
    private Long baiId;

    //项目id
    @Column
    private Long projectId;

    //组织id
    @Column
    private Long orgId;

    @Column(length = 100)
    @Schema(description = "检验方")
    private String inspectParties;

    @Schema(description = "报告内容")
    @Column(columnDefinition = "text")
    private String reports;

    @Schema(description = "外检遗留问题id")
    @Column(columnDefinition = "text")
    private String exInsIssueIds;

    @Schema(description = "内检遗留问题id")
    @Column(columnDefinition = "text")
    private String inInsIssueIds;

    @Column
    @Schema(description = "子报告 主报告关系")
    private String reportSubTypeInfo;

    public String getReportSubTypeInfo() {
        return reportSubTypeInfo;
    }

    public void setReportSubTypeInfo(String reportSubTypeInfo) {
        this.reportSubTypeInfo = reportSubTypeInfo;
    }

    @JsonProperty(value = "reportSubTypeInfo", access = JsonProperty.Access.READ_ONLY)
    public List<ReportSubTypeDTO> getJsonReportSubTypeInfo() {
        if (reportSubTypeInfo != null && !"".equals(reportSubTypeInfo)) {
            return StringUtils.decode(reportSubTypeInfo, new TypeReference<List<ReportSubTypeDTO>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonReportSubTypeInfo(List<ReportSubTypeDTO> reportSubTypeInfo) {
        if (reportSubTypeInfo != null) {
            this.reportSubTypeInfo = StringUtils.toJSON(reportSubTypeInfo);
        }
    }

    @JsonProperty(value = "exInsIssueIds", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonExInsIssueIdsReadOnly() {
        if (exInsIssueIds != null && !"".equals(exInsIssueIds)) {
            return StringUtils.decode(exInsIssueIds, new TypeReference<List<String>>() {
            });
        } else {
            return null;
        }
    }

    @JsonIgnore
    public void setJsonExInsIssueIds(List<String> exInsIssueIds) {
        if (exInsIssueIds != null) {
            this.exInsIssueIds = StringUtils.toJSON(exInsIssueIds);
        }
    }

    @JsonProperty(value = "inInsIssueIds", access = JsonProperty.Access.READ_ONLY)
    public List<Long> getJsonInInsIssueIdsReadOnly() {
        if (inInsIssueIds != null && !"".equals(inInsIssueIds)) {
            return StringUtils.decode(inInsIssueIds, new TypeReference<List<Long>>() {
            });
        } else {
            return null;
        }
    }

    @JsonIgnore
    public void setJsonInInsIssueIds(List<Long> inInsIssueIds) {
        if (inInsIssueIds != null) {
            this.inInsIssueIds = StringUtils.toJSON(inInsIssueIds);
        }
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getReports() {
        return reports;
    }

    public void setReports(String reports) {
        this.reports = reports;
    }

    @JsonIgnore
    public void setJsonReports(List<ActReportDTO> reports) {
        if (reports != null) {
            this.reports = StringUtils.toJSON(reports);
        }
    }

    @JsonIgnore
    public void addJsonReports(ActReportDTO report) {
        if (report != null) {
            List<ActReportDTO> reportList = getJsonReportsReadOnly();
            for (int i = reportList.size() - 1; i >= 0; i--) {
                if (reportList.get(i).getReportQrCode().equals(report.getReportQrCode())) {
                    reportList.remove(i);
                }
            }
            reportList.add(report);
            this.reports = StringUtils.toJSON(reportList);
        }
    }

    @JsonProperty(value = "reports", access = JsonProperty.Access.READ_ONLY)
    public List<ActReportDTO> getJsonReportsReadOnly() {
        if (reports != null && !"".equals(reports)) {
            return StringUtils.decode(reports, new TypeReference<List<ActReportDTO>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    public String getExInsIssueIds() {
        return exInsIssueIds;
    }

    public void setExInsIssueIds(String exInsIssueIds) {
        this.exInsIssueIds = exInsIssueIds;
    }

    public String getInInsIssueIds() {
        return inInsIssueIds;
    }

    public void setInInsIssueIds(String inInsIssueIds) {
        this.inInsIssueIds = inInsIssueIds;
    }

    @JsonProperty(value = "inspectParties", access = JsonProperty.Access.READ_ONLY)
    public List<InspectParty> getJsonInspectParties() {
        if (inspectParties != null && !"".equals(inspectParties)) {
            return StringUtils.decode(inspectParties, new TypeReference<List<InspectParty>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonParties(List<InspectParty> inspectParties) {
        if (inspectParties != null) {
            this.inspectParties = StringUtils.toJSON(inspectParties);
        }
    }

    public String getInspectParties() {
        return inspectParties;
    }

    public void setInspectParties(String inspectParties) {
        this.inspectParties = inspectParties;
    }

    public Long getBaiId() {
        return baiId;
    }

    public void setBaiId(Long baiId) {
        this.baiId = baiId;
    }
}
