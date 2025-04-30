package com.ose.tasks.dto.numeric;

import com.ose.util.StringUtils;
import com.ose.vo.unit.WeightUnit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 重量值对象。
 */
public class WeightDTO {

    private static final Pattern PATTERN = Pattern.compile(
        "(\\d+(\\.\\d+)?)\\s*(kg|g|mg|t)?",
        Pattern.CASE_INSENSITIVE
    );

    // 重量值
    private Double value = null;

    // 重量单位
    private WeightUnit unit = null;

    // 克数
    private Double grams = null;

    // 千克数
    private Double kgs = null;

    /**
     * 构造方法。
     */
    public WeightDTO(WeightUnit unit, String string) {

        string = StringUtils.trim(string);

        if (StringUtils.isEmpty(string)) {
            return;
        }

        Matcher matcher = PATTERN.matcher(string);

        if (matcher.matches()) {
            value = Double.valueOf(matcher.group(1));
            this.unit = WeightUnit.getByName(matcher.group(3));
        }

        if (this.unit == null) {
            this.unit = unit;
        }

        if (this.unit != null) {
            switch (this.unit) {
                case GRAM:
                    grams = value;
                    break;
//                case MILLIGRAM:
//                    grams = value / 1000;
//                    break;
                case KILOGRAM:
                    grams = value * 1000;
                    kgs = value;
                    break;
                case TON:
                    grams = value * 1000000;
                    kgs = value * 1000;
                    break;
                default:
                    break;
            }
        }

    }

    public Double getValue() {
        return value;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    public Double getGrams() {
        return grams;
    }

    public Double getKgs() {
        return kgs;
    }

}
