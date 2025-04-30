package com.ose.tasks.entity;

import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;

/**
 * @author: DaiZeFeng
 * @date: 2023/2/28
 */
@Entity
public class StageVersion extends BaseBizEntity {
    private static final long serialVersionUID = -5114214558357006419L;

    @Schema(description = "组织Id")
    private Long orgId;

    @Schema(description = "项目Id")
    private Long projectId;

    @Schema(description = "版本代码")
    private String versionCode;

    @Schema(description = "阶段")
    private String stage;

    @Schema(description = "阶段Id")
    private String stageId;

    @Schema(description = "备注")
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }
}
