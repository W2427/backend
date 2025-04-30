package com.ose.tasks.dto.wps;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class WpsJointTypeDTO extends BaseDTO {

    private static final long serialVersionUID = -721524128364175101L;

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
