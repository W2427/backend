package com.ose.vo.unit;

import com.ose.vo.ValueObject;
import com.ose.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 长度单位。
 */
public enum LengthUnit implements ValueObject {

    // 英寸
    INCH("in", "英寸", "\"", "inch", "inches", "in"),

    // 英尺
    FOOT("ft", "英尺", "'", "foot", "feet", "ft"),

    // 米
    METER("m", "米", "m", "meter", "meters", "m"),

    // 厘米
    CENTIMETER("cm", "厘米", "cm", "centimeter", "centimeters", "cm"),

    // 毫米
    MILLIMETER("mm", "毫米", "mm", "millimeter", "millimeters", "mm"),
    DN("dn", "公称直径", "dn", "dn", "dn", "dn");

    private String displayName;

    private String description;

    private String displayText;

    private List<String> names;

    LengthUnit(String displayName, String description, String displayText, String... names) {
        this.displayName = displayName;
        this.description = description;
        this.displayText = displayText;
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
        return displayText;
    }

    public static LengthUnit getByName(String name) {

        if (StringUtils.isEmpty(name)) {
            return null;
        }

        name = name.toLowerCase().replaceAll("\\s+", "_");

        for (LengthUnit unit : LengthUnit.values()) {
            if (unit.names.indexOf(name) >= 0) {
                return unit;
            }
        }

        return null;
    }

}
