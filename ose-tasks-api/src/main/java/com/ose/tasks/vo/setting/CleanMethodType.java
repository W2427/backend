package com.ose.tasks.vo.setting;

import com.ose.util.StringUtils;
import com.ose.vo.ValueObject;

/**
 * 清洁方式类型。
 */
public enum CleanMethodType implements ValueObject {

    WATER_FLUSH("water_flush"),
    OIL_FLUSH("oil_flush"),
    AIR_BLOW("air_blow");

    private String displayName;

    CleanMethodType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 根据名称取得枚举值。
     *
     * @param name 名称
     * @return 枚举值
     */
    public static CleanMethodType getByName(String name) {

        if (StringUtils.isEmpty(name)) {
            return null;
        }

        for (CleanMethodType type : CleanMethodType.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
