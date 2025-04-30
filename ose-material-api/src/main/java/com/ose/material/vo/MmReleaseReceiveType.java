package com.ose.material.vo;

import com.ose.util.StringUtils;
import com.ose.vo.ValueObject;

/**
 * 材料类型。
 */
public enum MmReleaseReceiveType implements ValueObject {

    ZERO("等于0", "等于0"),

    NOT_RECEIVE("未入库量", "未入库量"),

    REQUISITION("等于采购量", "等于采购量");


    private String displayName;

    private String description;


    MmReleaseReceiveType(String displayName, String description) {
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

    public static MmReleaseReceiveType getByDisplayName(String displayName) {

        if (StringUtils.isEmpty(displayName)) {
            return null;
        }

        for (MmReleaseReceiveType type : MmReleaseReceiveType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return null;
    }

}
