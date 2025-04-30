package com.ose.tasks.entity.workinghour;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.workinghour.ProjectWorkingHourStatusType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 项目工时履历实体类。
 */
@Entity
@Table(name = "wh_project_working_hour_history")
public class ProjectWorkingHourHistoryEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    private Long projectWorkingHourId;

    private String comments;

    @Column(nullable = false)
    private Long createdBy;

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

    public Long getProjectWorkingHourId() {
        return projectWorkingHourId;
    }

    public void setProjectWorkingHourId(Long projectWorkingHourId) {
        this.projectWorkingHourId = projectWorkingHourId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ProjectWorkingHourStatusType getWorkingHourStatus() {
        return workingHourStatus;
    }

    public void setWorkingHourStatus(ProjectWorkingHourStatusType workingHourStatus) {
        this.workingHourStatus = workingHourStatus;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * 取得创建者引用信息。
     *
     * @return 创建者引用信息
     */
    @Schema(description = "创建者信息")
    @JsonProperty(value = "createdBy")
    public ReferenceData getCreatedByRef() {
        return this.createdBy == null
            ? null
            : new ReferenceData(this.createdBy);
    }

    /**
     * 取得相关联的用户 ID 的集合。
     *
     * @return 相关联的用户 ID 的集合
     */
    @Override
    public Set<Long> relatedUserIDs() {

        Set<Long> userIDs = new HashSet<>();

        if (this.getCreatedBy() != null && this.getCreatedBy() != 0L) {
            userIDs.add(this.getCreatedBy());
        }

        return userIDs;
    }
}
