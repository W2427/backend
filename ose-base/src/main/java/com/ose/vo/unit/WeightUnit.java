package com.ose.vo.unit;

import com.ose.vo.ValueObject;
import com.ose.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 重量单位。
 */
public enum WeightUnit implements ValueObject {

//    // 毫克
//    MILLIGRAM("mg", "毫克", "milligram", "mg"),

    // 克
    GRAM("g", "克", "gram", "g"),

    // 千克
    KILOGRAM("kg", "千克", "kilogram", "kg"),

    // 吨
    TON("t", "吨", "ton", "t");

    private String displayName;

    private String description;

    private List<String> names;

    WeightUnit(String displayName, String description, String... names) {
        this.displayName = displayName;
        this.description = description;
        this.names = Arrays.asList(names);
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static WeightUnit getByName(String name) {

        if (StringUtils.isEmpty(name)) {
            return null;
        }

        name = name.toLowerCase().replaceAll("\\s+", "_");

        for (WeightUnit unit : WeightUnit.values()) {
            if (unit.names.indexOf(name) >= 0) {
                return unit;
            }
        }

        return null;
    }

}
