package com.ose.vo.unit;

import com.ose.vo.ValueObject;
import com.ose.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 面积单位。
 */
public enum AreaUnit implements ValueObject {

    // 平方英寸
    SQUARE_INCH("in²", "平方英寸", "square_inch", "in2"),

    // 平方英尺
    SQUARE_FOOT("ft²", "平方英尺", "square_foot", "foot2", "feet2", "ft2"),

    // 平方米
    SQUARE_METER("m²", "平方米", "square_meter", "meter2", "m2"),

    // 平方厘米
    SQUARE_CENTIMETER("cm²", "平方厘米", "square_centimeter", "centimeter2", "cm2"),

    // 平方毫米
    SQUARE_MILLIMETER("mm²", "平方毫米", "square_millimeter", "millimeter2", "mm2");

    private String displayName;

    private String description;

    private List<String> names;

    AreaUnit(String displayName, String description, String... names) {
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

    /**
     * 按名字取得对应Enum值
     *
     * @param name
     * @return
     */
    public static AreaUnit getByName(String name) {

        if (StringUtils.isEmpty(name)) {
            return null;
        }

        name = name.toLowerCase()
            .replaceAll("\\s+", "_")
            .replaceAll("²", "2");

        for (AreaUnit unit : AreaUnit.values()) {
            if (unit.names.indexOf(name) >= 0) {
                return unit;
            }
        }

        return null;
    }

}
