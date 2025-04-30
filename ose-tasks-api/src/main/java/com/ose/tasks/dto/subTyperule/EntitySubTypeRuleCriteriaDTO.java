package com.ose.tasks.dto.subTyperule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;

/**
 * 实体类型设置规则传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntitySubTypeRuleCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = 3753812239306672043L;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "实体大类型")
    private String entityType;

    @Schema(description = "实体子类型")
    private String entitySubType;

    @Schema(description = "实体业务类型")
    @Column(nullable = false, length = 64)
    private String entityBusinessType;

    @JsonCreator
    public EntitySubTypeRuleCriteriaDTO() {
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public String getEntityBusinessType() {
        return entityBusinessType;
    }

    public void setEntityBusinessType(String entityBusinessType) {
        this.entityBusinessType = entityBusinessType;
    }
}
