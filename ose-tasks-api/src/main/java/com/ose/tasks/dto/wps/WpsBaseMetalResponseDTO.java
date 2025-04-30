package com.ose.tasks.dto.wps;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class WpsBaseMetalResponseDTO extends BaseDTO {

    private static final long serialVersionUID = 9110349754386578182L;

    @Schema(description = "编号")
    private String code;

    @Schema(description = "编号别称")
    private String codeAlias;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeAlias() {
        return codeAlias;
    }

    public void setCodeAlias(String codeAlias) {
        this.codeAlias = codeAlias;
    }
}
