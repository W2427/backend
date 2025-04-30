package com.ose.material.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 采购单明细查询DTO
 */
public class MmPurchasePackageItemSearchDTO extends PageDTO {

    private static final long serialVersionUID = 7081169653864885648L;

    @Schema(description = "搜索关键字")
    public String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
