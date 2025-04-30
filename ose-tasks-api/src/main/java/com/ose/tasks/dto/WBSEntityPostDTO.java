package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 项目 WBS 实体资源添加数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WBSEntityPostDTO extends WBSEntityPatchDTO {

    private static final long serialVersionUID = -6378044393088162758L;

    @Schema(description = "实体的项目节点 ID")
    private Long projectNodeId;

    @Schema(description = "实体类型")
    private String entityType;

    @Schema(description = "实体 ID")
    private Long entityId;

    @Schema(description = "ISO图纸页码")
    private Integer dwgShtNo;

    @Schema(description = "工作量 物量")
    private Double workLoad;


    public Long getProjectNodeId() {
        return projectNodeId;
    }

    public void setProjectNodeId(Long projectNodeId) {
        this.projectNodeId = projectNodeId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }


    public Integer getDwgShtNo() {
        return dwgShtNo;
    }

    public void setDwgShtNo(Integer dwgShtNo) {
        this.dwgShtNo = dwgShtNo;
    }

    public Double getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(Double workLoad) {
        this.workLoad = workLoad;
    }

}
