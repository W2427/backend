package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * WELD实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeldEntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {

    private static final long serialVersionUID = 3753812239306672043L;

    @Schema(description = "焊口类型代码")
    private String weldType;

    @Schema(description = "焊口实体类型")
    private String weldEntityType;

    @Schema(description = "焊口材料分组代码")
    private String materialGroupCode;

    @Schema(description = "焊工ID")
    private Long welderId;

    @Schema(description = "是否已经做NDT，（true: 已做NDT，false: 未做NDT）")
    private String ndt;

    private List<Long> EntityIds;

    @Schema(description = "实体状态")
    private String status;

    @JsonCreator
    public WeldEntryCriteriaDTO() {
    }

    public String getWeldType() {
        return weldType;
    }

    @JsonSetter
    public void setWeldType(String weldType) {
        this.weldType = weldType;
    }

    public String getWeldEntityType() {
        return weldEntityType;
    }

    public void setWeldEntityType(String weldEntityType) {
        this.weldEntityType = weldEntityType;
    }

    public String getMaterialGroupCode() {
        return materialGroupCode;
    }

    public void setMaterialGroupCode(String materialGroupCode) {
        this.materialGroupCode = materialGroupCode;
    }

    public Long getWelderId() {
        return welderId;
    }

    public void setWelderId(Long welderId) {
        this.welderId = welderId;
    }

    public String getNdt() {
        return ndt;
    }

    public void setNdt(String ndt) {
        this.ndt = ndt;
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
