package com.ose.tasks.vo;


import com.ose.vo.ValueObject;

public enum WeldMaterialType implements ValueObject {
    FCAW_GS("FCAW_GS", "CO2焊材"),
    GTAW("GTAW", "不锈钢焊材"),
    SAW("SAW", "自动焊焊材"),
    SAW_FLUX("SAW_FLUX", "自动焊焊剂"),
    SMAW("SMAW", "手工焊焊材");

    private String displayName;

    private String description;

    WeldMaterialType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
