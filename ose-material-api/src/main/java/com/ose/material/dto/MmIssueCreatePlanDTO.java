package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 材料出库单创建DTO
 */
public class MmIssueCreatePlanDTO extends BaseDTO {

    private static final long serialVersionUID = 6634678674400722702L;

    @Schema(description = "计划id")
    private Long id;

    @Schema(description = "实体名称")
    private String name;

    @Schema(description = "实体类型")
    private String entityType;

    @Schema(description = "实体子类型")
    private String entitySubType;

    @Schema(description = "竖区")
    private String sector;

    @Schema(description = "阶段")
    private String stage;

    @Schema(description = "工序")
    private String process;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
