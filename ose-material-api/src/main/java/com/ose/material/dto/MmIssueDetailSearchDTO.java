package com.ose.material.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 出库单明细查询DTO
 */
public class MmIssueDetailSearchDTO extends PageDTO {

    private static final long serialVersionUID = 6298548428547570353L;

    @Schema(description = "搜索关键字")
    public String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
