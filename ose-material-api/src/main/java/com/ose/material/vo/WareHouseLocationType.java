package com.ose.material.vo;

import com.ose.vo.ValueObject;

/**
 * 仓库类型
 */
public enum WareHouseLocationType implements ValueObject {
    WAREHOUSE("仓库", "仓库"),
    AREA("区域", "区域"),
    LOCATION("位置", "位置"),
    SHELVES("货架", "货架");

    private String displayName;

    private String description;

    WareHouseLocationType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
