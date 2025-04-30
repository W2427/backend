package com.ose.tasks.dto.wps;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class WpsBevelTypeDTO extends BaseDTO {

    private static final long serialVersionUID = -4643798914041234981L;

    @Schema(description = "代码")
    private String code;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "备注")
    private String remark;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
