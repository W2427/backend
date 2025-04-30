package com.ose.tasks.vo.wbs;

/**
 * WBS 条目类型。
 */
public enum WBSEntryType {

    UNITS("FIXED_UNITS"),
    DURATION("FIXED_DURATION"),
    WORK("FIXED_WORK"),
    ENTITY("ENTITY");

/*    FIXED_UNITS,
    FIXED_DURATION,
    FIXED_WORK,
    FIXED_DURATION_AND_UNITS;*/

    private String code;

    WBSEntryType(String code) {
        this.code = code;
    }

    public static WBSEntryType getInstance(String code) {

        for (WBSEntryType wbsEntryType : WBSEntryType.values()) {
            if (wbsEntryType.code.equals(code)) {
                return wbsEntryType;
            }
        }

        return UNITS;
    }

}
