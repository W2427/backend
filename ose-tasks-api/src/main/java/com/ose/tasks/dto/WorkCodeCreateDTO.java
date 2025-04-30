package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class WorkCodeCreateDTO extends BaseDTO {

    @Schema(description = "工作项代码")
    private Integer workCode;

    @Schema(description = "工作项代码描述")
    private String wcDesc;

    public Integer getWorkCode() {
        return workCode;
    }

    public void setWorkCode(Integer workCode) {
        this.workCode = workCode;
    }

    public String getWcDesc() {
        return wcDesc;
    }

    public void setWcDesc(String wcDesc) {
        this.wcDesc = wcDesc;
    }
}
