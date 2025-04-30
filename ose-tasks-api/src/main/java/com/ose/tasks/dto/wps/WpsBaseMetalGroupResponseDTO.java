package com.ose.tasks.dto.wps;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class WpsBaseMetalGroupResponseDTO extends BaseDTO {

    private static final long serialVersionUID = -5258886941267856975L;

    @Schema(description = "编号")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
