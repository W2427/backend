package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public class ExInspViewCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = 8194574818310660466L;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "操作员")
    private Long operator;

    @Schema(description = "状态(MID/DONE)")
    private EntityStatus state;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public EntityStatus getState() {
        return state;
    }

    public void setState(EntityStatus state) {
        this.state = state;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

}
