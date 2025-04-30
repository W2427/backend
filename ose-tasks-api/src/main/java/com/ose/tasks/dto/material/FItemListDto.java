package com.ose.tasks.dto.material;

import com.ose.dto.PageDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 入库单查询DTO
 */
public class FItemListDto extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "入库单")
    private String fmrrCode;

    public String getFmrrCode() {
        return fmrrCode;
    }

    public void setFmrrCode(String fmrrCode) {
        this.fmrrCode = fmrrCode;
    }

}
