package com.ose.tasks.dto.drawing;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.ConstructionChangeType;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 生产设计图纸清单导入数据传输对象。
 */
public class DesignChangeCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "建造变更类型")
    @Enumerated(EnumType.STRING)
    private ConstructionChangeType changeType;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ConstructionChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ConstructionChangeType changeType) {
        this.changeType = changeType;
    }

}
