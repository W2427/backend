package com.ose.tasks.dto.bpm;

import io.swagger.v3.oas.annotations.media.Schema;

public class ExInspScheduleCriteriaDTO extends BaseBatchTaskCriteriaDTO {

    private static final long serialVersionUID = 8194574818310660466L;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "区分")
    private String coordinateCategory;

    @Schema(description = "状态")
    private String state;

    @Schema(description = "当前人id")
    private Long operator;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCoordinateCategory() {
        return coordinateCategory;
    }

    public void setCoordinateCategory(String coordinateCategory) {
        this.coordinateCategory = coordinateCategory;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }
}
