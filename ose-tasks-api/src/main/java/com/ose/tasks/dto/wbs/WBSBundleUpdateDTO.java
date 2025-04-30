package com.ose.tasks.dto.wbs;

import com.ose.annotation.NullableNotBlank;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Size;

/**
 * WBS 条目打包更新数据传输对象。
 */
public class WBSBundleUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = 8936369658752913651L;

    @Schema(description = "实体类型")
    private String entityType;

    @Schema(description = "实体子类型")
    @NullableNotBlank
    @Size(max = 32)
    private String entitySubType;

    @Schema(description = "工序阶段")
    @NullableNotBlank
    @Size(max = 64)
    private String stage;

    @Schema(description = "工序")
    @NullableNotBlank
    @Size(max = 64)
    private String process;

    @Schema(description = "工作场地 ID")
    @NullableNotBlank
    @Size(max = 16)
    private Long workSiteId;

    @Schema(description = "工作组 ID")
    @NullableNotBlank
    private Long teamId;

    @Schema(description = "备注")
    @NullableNotBlank
    @Size(max = 255)
    private String remarks;

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

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
