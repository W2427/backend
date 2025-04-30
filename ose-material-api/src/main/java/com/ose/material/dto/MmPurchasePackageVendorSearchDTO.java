package com.ose.material.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 采购包添加供货商DTO
 */
public class MmPurchasePackageVendorSearchDTO extends PageDTO {

    private static final long serialVersionUID = 2336457637428658756L;

    @Schema(description = "搜索关键字")
    public String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
