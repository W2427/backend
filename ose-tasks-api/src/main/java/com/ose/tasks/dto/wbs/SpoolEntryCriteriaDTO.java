package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.tasks.vo.material.NestGateWay;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * ISO实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpoolEntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {

    private static final long serialVersionUID = 3753812239306672043L;

    @Schema(description = "iso父级id")
    private Long isoId;

    @Schema(description = "管材已领用")
    private Boolean used;

    @Schema(description = "套料状态")
    private NestGateWay nestGateWay;

    private List<Long> EntityIds;

    @Schema(description = "添加状态")
    private String addStatus;

    @Schema(description = "实体状态")
    private String status;

    @JsonCreator
    public SpoolEntryCriteriaDTO() {
    }

    public Long getIsoId() {
        return isoId;
    }

    public void setIsoId(Long isoId) {
        this.isoId = isoId;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public NestGateWay getNestGateWay() {
        return nestGateWay;
    }

    public void setNestGateWay(NestGateWay nestGateWay) {
        this.nestGateWay = nestGateWay;
    }

    public List<Long> getEntityIds() {
        return EntityIds;
    }

    public void setEntityIds(List<Long> entityIds) {
        EntityIds = entityIds;
    }

    public String getAddStatus() {
        return addStatus;
    }

    public void setAddStatus(String addStatus) {
        this.addStatus = addStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
