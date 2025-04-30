package com.ose.material.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 在库材料明细查询DTO
 */
public class MmMaterialInStockDetailSearchDTO extends PageDTO {

    private static final long serialVersionUID = -144057424754518333L;

    @Schema(description = "搜索关键字")
    public String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
