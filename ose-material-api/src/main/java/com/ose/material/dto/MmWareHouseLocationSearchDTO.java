package com.ose.material.dto;

import com.ose.dto.PageDTO;
import com.ose.material.vo.WareHouseLocationType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * BOM创建DTO
 */
public class MmWareHouseLocationSearchDTO extends PageDTO {

    private static final long serialVersionUID = 4606234526958793079L;

    @Schema(description = "搜索关键字")
    public String keyword;

    @Schema(description = "父级仓库ID")
    public String parentWareHouseId;

    @Schema(description = "仓库类型")
    public String type;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getParentWareHouseId() {
        return parentWareHouseId;
    }

    public void setParentWareHouseId(String parentWareHouseId) {
        this.parentWareHouseId = parentWareHouseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
