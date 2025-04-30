package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * ISO实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PipePieceEntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {

    @Schema(description = "父级实体id")
    private Long parentEntityId;

    private List<Long> EntityIds;

    @Schema(description = "实体状态")
    private String status;

    private static final long serialVersionUID = 3753812239306672043L;

    @JsonCreator
    public PipePieceEntryCriteriaDTO() {
    }

    public Long getParentEntityId() {
        return parentEntityId;
    }

    public void setParentEntityId(Long parentEntityId) {
        this.parentEntityId = parentEntityId;
    }

    public List<Long> getEntityIds() {
        return EntityIds;
    }

    public void setEntityIds(List<Long> entityIds) {
        EntityIds = entityIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
