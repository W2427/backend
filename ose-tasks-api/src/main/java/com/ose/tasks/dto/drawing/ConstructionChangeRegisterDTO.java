package com.ose.tasks.dto.drawing;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.ConstructionChangeType;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 实体类。
 */
public class ConstructionChangeRegisterDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    @Schema(description = "变更类型")
    @Enumerated(EnumType.STRING)
    private ConstructionChangeType changeType;

    @Schema(description = "模块名称")
    private String modelName;

    @Schema(description = "修改原因")
    private String originatedBy;

    @Schema(description = "具体行动")
    private String actions;

    public ConstructionChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ConstructionChangeType changeType) {
        this.changeType = changeType;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getOriginatedBy() {
        return originatedBy;
    }

    public void setOriginatedBy(String originatedBy) {
        this.originatedBy = originatedBy;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

}
