package com.ose.material.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * BOM创建DTO
 */
public class MmMaterialCodeTypeSearchDTO extends PageDTO {

    private static final long serialVersionUID = 4606234526958793079L;

    @Schema(description = "搜索关键字")
    public String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
