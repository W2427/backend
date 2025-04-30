package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class ActivitiModelCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = 8194574818310660466L;

    @Schema(description = "名称/流程定义KEY")
    private String search;

    @Schema(description = "运行状态")
    private String suspensionState;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(String suspensionState) {
        this.suspensionState = suspensionState;
    }

}
