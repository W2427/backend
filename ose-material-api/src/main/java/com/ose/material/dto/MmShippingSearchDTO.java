package com.ose.material.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 发货单查询DTO
 */
public class MmShippingSearchDTO extends PageDTO {

    private static final long serialVersionUID = 5669448425688771722L;

    @Schema(description = "搜索关键字")
    public String keyword;

    @Schema(description = "搜索关键字")
    public String keywordA;

    @Schema(description = "搜索关键字")
    public String keywordB;

    public String getKeywordA() {
        return keywordA;
    }

    public void setKeywordA(String keywordA) {
        this.keywordA = keywordA;
    }

    public String getKeywordB() {
        return keywordB;
    }

    public void setKeywordB(String keywordB) {
        this.keywordB = keywordB;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
