package com.ose.material.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;

/**
 * 余料查询DTO
 */
public class MmSurplusMaterialSearchDTO extends PageDTO {

    @Serial
    private static final long serialVersionUID = -5777665058082505861L;

    @Schema(description = "搜索关键字")
    public String keyword;

    @Schema(description = "出库状态（已出库，未出库）")
    private Boolean issuedFlg;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Boolean getIssuedFlg() {
        return issuedFlg;
    }

    public void setIssuedFlg(Boolean issuedFlg) {
        this.issuedFlg = issuedFlg;
    }
}
