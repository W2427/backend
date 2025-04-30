package com.ose.tasks.dto.wps;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class WpsBevelTypeResponseDTO extends BaseDTO {

    private static final long serialVersionUID = 2100491864981077667L;

    @Schema(description = "编号")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
