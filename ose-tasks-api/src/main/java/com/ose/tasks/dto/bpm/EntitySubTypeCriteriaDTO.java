package com.ose.tasks.dto.bpm;

import com.ose.dto.PageDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体工序列表查询DTO
 */
public class EntitySubTypeCriteriaDTO extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "名称(中文/英文)")
    private String name;

    @Schema(description = "实体类型ID")
    private Long entityTypeId;

    @Schema(description = "实体业务类型ID")
    private Long entityBusinessTypeId;

    @Schema(description = "实体类型名称")
    private String entityTypeName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getEntityBusinessTypeId() {
        return entityBusinessTypeId;
    }

    public void setEntityBusinessTypeId(Long entityBusinessTypeId) {
        this.entityBusinessTypeId = entityBusinessTypeId;
    }

    public Long getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(Long entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public String getEntityTypeName() {
        return entityTypeName;
    }

    public void setEntityTypeName(String entityTypeName) {
        this.entityTypeName = entityTypeName;
    }
}
