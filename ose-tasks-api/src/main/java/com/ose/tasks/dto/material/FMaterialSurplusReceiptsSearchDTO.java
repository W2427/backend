package com.ose.tasks.dto.material;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 材料准备单DTO
 */
public class FMaterialSurplusReceiptsSearchDTO extends PageDTO {

    private static final long serialVersionUID = -2556700003727268613L;

    @Schema(description = "关键字")
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
