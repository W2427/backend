package com.ose.tasks.dto.wbs;

import com.ose.dto.PageDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class SpoolMergeSearchDTO extends PageDTO {
    private static final long serialVersionUID = -9101443167638143487L;

    @Schema(description = "实体编号")
    private String no;

    @Schema(description = "实体id")
    private Long entityId;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
