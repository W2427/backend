package com.ose.tasks.vo;

import com.ose.util.StringUtils;
import com.ose.vo.ValueObject;

/**
 * 项目条目类型。
 */
public enum DesignChangeDisciplines implements ValueObject {

    NA("NA"),
    ST("ST"),
    OU("OU"),
    ACCOM("ACCOM"),
    MECH("Mech"),
    PIPE("PIPE"),
    HVAC("HVAC"),
    ELEC("ELEC"),
    COAT("COAT");

    private String displayName;

    DesignChangeDisciplines(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public static DesignChangeDisciplines getByDisplayName(String displayName) {

        if (StringUtils.isEmpty(displayName)) {
            return null;
        }

        for (DesignChangeDisciplines type : DesignChangeDisciplines.values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return null;
    }

}
