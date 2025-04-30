package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class SubWorkClassFollowCreateDTO extends BaseDTO {

    @Schema(description = "工作项代码")
    private String workClass;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "单位")
    private String unitColumn;

    @Schema(description = "模块")
    private String module;

    public String getWorkClass() {
        return workClass;
    }

    public void setWorkClass(String workClass) {
        this.workClass = workClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitColumn() {
        return unitColumn;
    }

    public void setUnitColumn(String unitColumn) {
        this.unitColumn = unitColumn;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
