package com.ose.tasks.dto.numeric;

import com.ose.util.StringUtils;
import com.ose.vo.unit.TemperatureUnit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 温度值对象。
 */
public class TemperatureDTO {

    private static final Pattern PATTERN = Pattern.compile(
        "(-)?(\\d+(\\.\\d+)?)\\s*(C|℃|F|K|°[CF]?)?",
        Pattern.CASE_INSENSITIVE
    );

    // 温度值
    private Double value = null;

    // 温度单位
    private TemperatureUnit unit = null;

    // 摄氏度转换值
    private Double celsius = null;

    /**
     * 构造方法。
     */
    public TemperatureDTO(TemperatureUnit unit, String string) {
        if(StringUtils.isEmpty(string)) {
            this.unit = unit;
            value = 0.0;
            return;
        }

        Matcher matcher = PATTERN.matcher(string);

        if (!matcher.matches()) {
            return;
        }

        value = Double.valueOf(matcher.group(2));

        this.unit = TemperatureUnit.getByName(matcher.group(3));
        this.unit = this.unit == null ? unit : this.unit;

        if (this.unit == null) {
            return;
        }

        switch (this.unit) {
            case C:
                celsius = value;
                break;
            case F:
                celsius = (value - 32) * 5 / 9;
                break;
            case K:
                celsius = value = 273.15;
                break;
            default:
                break;
        }

    }

    public Double getValue() {
        return value;
    }

    public TemperatureUnit getUnit() {
        return unit;
    }

    public Double getCelsius() {
        return celsius;
    }

}
