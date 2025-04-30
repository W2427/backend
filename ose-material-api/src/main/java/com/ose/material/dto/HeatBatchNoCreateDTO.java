package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 炉批号创建DTO
 */
public class HeatBatchNoCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -512527412345120956L;

    @Schema(description = "组织ID")
    public Long orgId;

    @Schema(description = "项目ID")
    public Long projectId;

    @Schema(description = "炉号")
    private String heatNoCode;

    @Schema(description = "批号")
    private String batchNoCode;

    @Schema(description = "炉批号说明")
    private String desc;

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

    public String getHeatNoCode() {
        return heatNoCode;
    }

    public void setHeatNoCode(String heatNoCode) {
        this.heatNoCode = heatNoCode;
    }

    public String getBatchNoCode() {
        return batchNoCode;
    }

    public void setBatchNoCode(String batchNoCode) {
        this.batchNoCode = batchNoCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
