package com.ose.material.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 采购单查询DTO
 */
public class MmPurchasePackageSearchDTO extends PageDTO {

    private static final long serialVersionUID = -6947587314805392469L;

    @Schema(description = "搜索关键字")
    public String keyword;

    @Schema(description = "锁定状态")
    private Boolean locked;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }
}
