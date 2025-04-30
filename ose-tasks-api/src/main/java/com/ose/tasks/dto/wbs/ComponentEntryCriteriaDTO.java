package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 管件实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComponentEntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {

    private static final long serialVersionUID = 3753812239306672043L;

    @Schema(description = "管件实体类型")
    private String componentEntityType;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "父级实体id")
    private Long parentEntityId;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "壁厚等级")
    private String thickness;

    @Schema(description = "添加状态")
    private String addStatus;

    private List<Long> EntityIds;

    @Schema(description = "实体状态")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonCreator
    public ComponentEntryCriteriaDTO() {
    }

    public String getComponentEntityType() {
        return componentEntityType;
    }

    // 需要在焊口的焊接类型和焊接阶段之后赋值
    public void setComponentEntityType(String componentEntityType) {
        this.componentEntityType = componentEntityType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @JsonGetter
    public String getNpsText() {
        return npsText;
    }

    @JsonSetter
    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
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

    public String getAddStatus() {
        return addStatus;
    }

    public void setAddStatus(String addStatus) {
        this.addStatus = addStatus;
    }
}
