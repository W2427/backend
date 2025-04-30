package com.ose.tasks.dto.wps;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class WpsJointTypeResponseDTO extends BaseDTO {

    private static final long serialVersionUID = -6292257869843828589L;

    @Schema(description = "编号")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
