package com.ose.tasks.entity.wbs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * WBS 条目打包数据实体。
 */
@Entity
@Table(name = "wbs_bundle")
public class WBSBundle extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -695489425526246926L;

    @Schema(description = "组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "实体类型")
    @Column(nullable = false, length = 32)

    private String entityType;

    @Schema(description = "实体子类型")
    @Column(nullable = false, length = 32)
    private String entitySubType;

    @Schema(description = "工序阶段")
    @Column(nullable = false, length = 64)
    private String stage;

    @Schema(description = "工序")
    @Column(nullable = false, length = 64)
    private String process;

    @Schema(description = "工作场地 ID")
    @Column
    private Long workSiteId;

    @Schema(description = "工作场地名称")
    @Column
    private String workSiteName;

    @JsonIgnore
    @Column
    private Long teamId;

    @Schema(description = "备注")
    @Column
    private String remarks;

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

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Long getWorkSiteId() {
        return workSiteId;
    }

    public void setWorkSiteId(Long workSiteId) {
        this.workSiteId = workSiteId;
    }

    public String getWorkSiteName() {
        return workSiteName;
    }

    public void setWorkSiteName(String workSiteName) {
        this.workSiteName = workSiteName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    @JsonSetter
    public void setTeamId(ReferenceData teamRef) {
        this.teamId = teamRef == null ? null : teamRef.get$ref();
    }

    @Schema(description = "工作组 ID")
    @JsonProperty(value = "teamId")
    public ReferenceData getTeamRef() {
        return teamId == null ? null : new ReferenceData(teamId);
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public Set<Long> relatedOrgIDs() {

        Set<Long> orgIDs = new HashSet<>();

        if (teamId != null) {
            orgIDs.add(teamId);
        }

        return orgIDs;
    }

}
