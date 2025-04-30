package com.ose.tasks.dto.subTyperule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.wbs.EntityTypeRule;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * 实体类型设置规则 数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntitySubTypeRuleInsertDTO extends BaseDTO {

    private static final long serialVersionUID = 3753812239306672043L;

    @Schema(description = "实体大类型")
    private String entityType;

    @Schema(description = "实体子类型")
    private String subType;

    @Schema(description = "实体子类型ID")
    private Long entitySubTypeId;

    @Schema(description = "实体类型规则")
    @Enumerated(EnumType.STRING)
    private EntityTypeRule ruleType;

    @Schema(description = "实体类型规则顺序")
    private Integer ruleOrder;

    @Schema(description = "条件值1")
    private String value1;

    @Schema(description = "条件值2")
    private String value2;

    @Schema(description = "父级类型")
    private String parentType;

    @Schema(description = "是否需要保温厚度信息")
    private Boolean thicknessRequired;

    @JsonCreator
    public EntitySubTypeRuleInsertDTO() {
    }

    public String getEntityType() {
        return this.entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public EntityTypeRule getRuleType() {
        return ruleType;
    }

    public void setRuleType(EntityTypeRule ruleType) {
        this.ruleType = ruleType;
    }

    public Integer getRuleOrder() {
        return ruleOrder;
    }

    public void setRuleOrder(Integer ruleOrder) {
        this.ruleOrder = ruleOrder;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public Boolean getThicknessRequired() {
        return thicknessRequired;
    }

    public void setThicknessRequired(Boolean thicknessRequired) {
        this.thicknessRequired = thicknessRequired;
    }
}
