package com.ose.material.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 发货单明细下的查询DTO
 */
public class MmShippingDetailSearchDTO extends PageDTO {


    private static final long serialVersionUID = -6937953813365325973L;
    @Schema(description = "搜索关键字")
    public String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
