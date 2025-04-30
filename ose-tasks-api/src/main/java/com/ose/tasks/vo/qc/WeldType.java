package com.ose.tasks.vo.qc;

import com.ose.vo.ValueObject;

/**
 * 焊口类型。
 */
public enum WeldType implements ValueObject {

    BND("BND"),
    FLN("FLN"),
    FW("FW"),
    RPD("RPD"),
    THD("THD"),
    BW("BW"),
    SW("SW"),
    SOF("SOF"),
    LET("LET"),
    SP("SP"),
    SUP("SUP"),
    SOB("SOB"),
    TRN("TRN");

    private String displayName;

    WeldType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
