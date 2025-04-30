package com.ose.material.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 入库材料二维码查询DTO
 */
public class MmReleaseReceiveQrCodeSearchDTO extends PageDTO {

    private static final long serialVersionUID = 7408748687390456074L;

    @Schema(description = "搜索关键字")
    public String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
