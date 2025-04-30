package com.ose.tasks.dto.numeric;

import com.ose.util.StringUtils;
import com.ose.vo.unit.AreaUnit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 面积值对象。
 */
public class AreaDTO {

    private static final Pattern PATTERN = Pattern.compile(
        "(\\d+(\\.\\d+)?)\\s*([_a-z0-9]+)?",
        Pattern.CASE_INSENSITIVE
    );

    // 面积值
    private Double value = null;

    // 面积单位
    private AreaUnit unit = null;

    // 平方毫米
    private Double squareMillimeters = null;

    /**
     * 构造方法。
     */
    public AreaDTO(AreaUnit unit, String string) {

        string = StringUtils.trim(string);

        if (StringUtils.isEmpty(string)) {
            return;
        }

        Matcher matcher = PATTERN.matcher(string);

        if (matcher.matches()) {
            value = Double.valueOf(matcher.group(1));
            this.unit = AreaUnit.getByName(matcher.group(3));
        }

        if (this.unit == null) {
            this.unit = unit;
        }

        if (this.unit != null) {
            switch (this.unit) {
                case SQUARE_INCH:
                    squareMillimeters = value * 645.16;
                    break;
                case SQUARE_FOOT:
                    squareMillimeters = value * 92903.04;
                    break;
                case SQUARE_METER:
                    squareMillimeters = value * 1000000;
                    break;
                case SQUARE_CENTIMETER:
                    squareMillimeters = value * 100;
                    break;
                case SQUARE_MILLIMETER:
                    squareMillimeters = value;
                    break;
                default:
                    break;
            }
        }

    }

    public Double getValue() {
        return value;
    }

    public AreaUnit getUnit() {
        return unit;
    }

    public Double getSquareMillimeters() {
        return squareMillimeters;
    }

}
