package com.ose.material.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 供货商查询DTO
 */
public class MmVendorSearchDTO extends PageDTO {

    private static final long serialVersionUID = 8879957386370659287L;

    @Schema(description = "搜索关键字")
    public String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
