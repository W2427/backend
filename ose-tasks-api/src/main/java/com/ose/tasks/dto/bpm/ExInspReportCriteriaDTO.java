package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class ExInspReportCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = 8194574818310660466L;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "操作员")
    private Long operator;

    @Schema(description = "状态(UNDONE/DONE)")
    private String state;

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
