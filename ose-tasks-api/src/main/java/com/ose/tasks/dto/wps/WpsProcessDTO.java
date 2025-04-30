package com.ose.tasks.dto.wps;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class WpsProcessDTO extends BaseDTO {

    private static final long serialVersionUID = 6797563904867623875L;

    @Schema(description = "编号")
    private String code;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "适用范围")
    private String scope;

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

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
