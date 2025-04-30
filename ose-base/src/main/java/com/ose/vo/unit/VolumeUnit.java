package com.ose.vo.unit;

import com.ose.vo.ValueObject;
import com.ose.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 体积单位。
 */
public enum VolumeUnit implements ValueObject {

    // 吨位
    TONNAGE("tonnage", "吨位", "tonnage", "t"),

    // 立方米
    CUBIC_METER("m³", "立方米", "cubic_meter", "meter3", "m3"),

    // 升
    LITRE("L", "升", "litre", "l"),

    // 毫升（立方厘米）
    MILLILITRE("mL", "毫升", "millilitre", "ml", "cubic_centimeter", "centimeter3", "cm3");

    private String displayName;

    private String description;

    private List<String> names;

    VolumeUnit(String displayName, String description, String... names) {
        this.displayName = displayName;
        this.description = description;
        this.names = Arrays.asList(names);
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static VolumeUnit getByName(String name) {

        if (StringUtils.isEmpty(name)) {
            return null;
        }

        name = name.toLowerCase()
            .replaceAll("\\s+", "_")
            .replaceAll("³", "3");

        for (VolumeUnit unit : VolumeUnit.values()) {
            if (unit.names.indexOf(name) >= 0) {
                return unit;
            }
        }

        return null;
    }

}
