package com.ose.tasks.entity.workinghour;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.vo.workinghour.ProjectWorkingHourStatusType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 项目工时实体类。
 */
@Entity
@Table(name = "wh_project_working_hour")
public class ProjectWorkingHourEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Temporal(TemporalType.DATE)
    private Date projectWorkingHourDate;

    private Long projectWorkingHoursLargeCategory;

    private Long projectWorkingHoursSmallCategory;

    private String workingHoursTaskPackage;

    private String relatedDrawings;

    private String workContent;

    private Double workingHours;

    private String approvalRole;

    private Long approvalId;

    @Enumerated(EnumType.STRING)
    private ProjectWorkingHourStatusType workingHourStatus;

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

    public Date getProjectWorkingHourDate() {
        return projectWorkingHourDate;
    }

    public void setProjectWorkingHourDate(Date projectWorkingHourDate) {
        this.projectWorkingHourDate = projectWorkingHourDate;
    }

    public Long getProjectWorkingHoursLargeCategory() {
        return projectWorkingHoursLargeCategory;
    }

    public void setProjectWorkingHoursLargeCategory(Long projectWorkingHoursLargeCategory) {
        this.projectWorkingHoursLargeCategory = projectWorkingHoursLargeCategory;
    }

    public Long getProjectWorkingHoursSmallCategory() {
        return projectWorkingHoursSmallCategory;
    }

    public void setProjectWorkingHoursSmallCategory(Long projectWorkingHoursSmallCategory) {
        this.projectWorkingHoursSmallCategory = projectWorkingHoursSmallCategory;
    }

    public String getWorkingHoursTaskPackage() {
        return workingHoursTaskPackage;
    }

    public void setWorkingHoursTaskPackage(String workingHoursTaskPackage) {
        this.workingHoursTaskPackage = workingHoursTaskPackage;
    }

    public String getRelatedDrawings() {
        return relatedDrawings;
    }

    public void setRelatedDrawings(String relatedDrawings) {
        this.relatedDrawings = relatedDrawings;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public Double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Double workingHours) {
        this.workingHours = workingHours;
    }

    public String getApprovalRole() {
        return approvalRole;
    }

    public void setApprovalRole(String approvalRole) {
        this.approvalRole = approvalRole;
    }

    public ProjectWorkingHourStatusType getWorkingHourStatus() {
        return workingHourStatus;
    }

    public void setWorkingHourStatus(ProjectWorkingHourStatusType workingHourStatus) {
        this.workingHourStatus = workingHourStatus;
    }

    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }

    @Schema(description = "项目工时大类信息")
    @JsonProperty(value = "projectWorkingHoursLargeCategory", access = READ_ONLY)
    public ReferenceData getProjectWorkingHoursLargeCategoryRef() {
        return this.projectWorkingHoursLargeCategory == null
            ? null
            : new ReferenceData(this.projectWorkingHoursLargeCategory);
    }

    @Schema(description = "项目工时小类信息")
    @JsonProperty(value = "projectWorkingHoursSmallCategory", access = READ_ONLY)
    public ReferenceData getProjectWorkingHoursSmallCategoryRef() {
        return this.projectWorkingHoursSmallCategory == null
            ? null
            : new ReferenceData(this.projectWorkingHoursSmallCategory);
    }

    @Schema(description = "审核人信息")
    @JsonProperty(value = "approvalId", access = READ_ONLY)
    public ReferenceData getApprovalIdRef() {
        return this.approvalId == null
            ? null
            : new ReferenceData(this.approvalId);
    }

    /**
     * 取得相关联的用户 ID 的集合。
     *
     * @return 相关联的用户 ID 的集合
     */
    @Override
    public Set<Long> relatedUserIDs() {

        Set<Long> userIDs = new HashSet<>();

        if (this.getApprovalId() != null && this.getApprovalId() != 0L) {
            userIDs.add(this.getApprovalId());
        }

        return userIDs;
    }
}
