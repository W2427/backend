package com.ose.report.vo.log;

import com.ose.util.StringUtils;
import com.ose.vo.ValueObject;

/**
 * 无损探伤类型。
 */
public enum NDEType implements ValueObject {

    RT("RT"),
    PT("PT"),
    MT("MT"),
    UT("UT"),
    UT_MT("UT+MT");

    private String displayName;

    NDEType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public static NDEType getByDisplayName(String displayName) {

        if (StringUtils.isEmpty(displayName)) {
            return null;
        }

        for (NDEType type : NDEType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return null;
    }
}
