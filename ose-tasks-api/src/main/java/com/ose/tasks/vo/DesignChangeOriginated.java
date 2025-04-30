package com.ose.tasks.vo;

import com.ose.util.StringUtils;
import com.ose.vo.ValueObject;

/**
 * 项目条目类型。
 */
public enum DesignChangeOriginated implements ValueObject {

    DETAILED_DESIGN("Detailed Design"),
    PRODUCTION("Production"),
    OWNER("Owner"),
    CONSTRUCTION("Construction"),
    VENDOR("Vendor"),
    PROCUREMENT("Procurement");

    private String displayName;

    DesignChangeOriginated(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public static DesignChangeOriginated getByDisplayName(String displayName) {

        if (StringUtils.isEmpty(displayName)) {
            return null;
        }

        for (DesignChangeOriginated type : DesignChangeOriginated.values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return null;
    }

}
