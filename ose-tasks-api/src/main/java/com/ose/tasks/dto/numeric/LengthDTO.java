package com.ose.tasks.dto.numeric;

import com.ose.exception.ValidationError;
import com.ose.util.StringUtils;
import com.ose.vo.unit.LengthUnit;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 长度值对象。
 */
public class LengthDTO {

    // 长度单位名称
    private static final Map<String, String> UNITS = new HashMap<>();

    // 长度单位转换毫米的转换倍率
    public static final Map<String, Double> EQUIVALENCES = new HashMap<>();

    static {
        UNITS.put("feet", "feet");
        UNITS.put("ft", "feet");
        UNITS.put("'", "feet");
        UNITS.put("inches", "inches");
        UNITS.put("inch", "inches");
        UNITS.put("in", "inches");
        UNITS.put("\"", "inches");
        UNITS.put("m", "m");
        UNITS.put("cm", "cm");
        UNITS.put("mm", "mm");
        UNITS.put("dn", "dn");
        EQUIVALENCES.put("feet", 300.0);
        EQUIVALENCES.put("inches", 25.0);
        EQUIVALENCES.put("m", 1000.0);
        EQUIVALENCES.put("cm", 10.0);
        EQUIVALENCES.put("mm", 1.0);
        EQUIVALENCES.put("dn", 1.0);

    }

    /**
     * 长度格式。
     * 示例：
     * 12
     * 3.45
     * 67.8mm
     * 5'4.3 (5 英尺 4.3 英寸)
     * 9'12"
     * 1 2/3" (1 + 2/3 英寸)
     * 4'5.6/7" (4 英尺 5 + 6/7 英寸)
     */
    // TODO 正则不识别 3/4" 这样的长度 ((\d+)(((\.|\s+)(\d+)/(\d+))|(\.(\d+)))?)\s*(feet|ft|'|inches|inch|in|"|mm|cm|m)?

    private static final Pattern PATTERN = Pattern.compile(
        "(((\\d+)?((\\.|\\s+)?(\\d+)/(\\d+)))|(\\d+(\\.\\d+)?))\\s*(feet|ft|'|inches|inch|in|\"|mm|cm|m)?",
        Pattern.CASE_INSENSITIVE
    );

    // 长度值
    private Double value = null;

    // 长度单位
    private LengthUnit unit = null;

    // 毫米转换值
    private Double millimeters = 0.0;

    // 长度表示值
    private String lengthText = "";

    /**
     * 构造方法。
     */
    public LengthDTO(LengthUnit unit, String string) {

        string = StringUtils.trim(string);

        if (unit == null) throw new ValidationError("length unit is empty");

        if (StringUtils.isEmpty(string)) {
            return;
        }

        Matcher matcher = PATTERN.matcher(string);
        String group;
        Matcher groupMatcher;
        String number, integer, dividend, divisor, decimal, groupUnit = null;
        // 数值计算精度问题，数据类型改成BigDecimal
        BigDecimal value = BigDecimal.ZERO, equivalence, mm = BigDecimal.ZERO;

        while (matcher.find()) {
            value = BigDecimal.ZERO;

            group = matcher.group();

            //
            if (StringUtils.isEmpty(group)) {
                continue;
            }
            groupMatcher = PATTERN.matcher(group);

            if (!groupMatcher.matches()) {
                continue;
            }

            // 带分数数值的全部内容
            number = groupMatcher.group(2);
            // 带分数数值的整数部分
            integer = groupMatcher.group(3);
            // 带分数数值的分子部分
            dividend = groupMatcher.group(6);
            // 带分数数值的分母部分
            divisor = groupMatcher.group(7);
            // 不带分数部分的数值
            decimal = groupMatcher.group(8);
            // 数值单位
            // groupUnit = groupMatcher.group(10);

            // 带分数数值
            if (number != null) {
                if (integer != null) {
                    value = new BigDecimal(integer);
                }
                if ((dividend != null) && (divisor != null)) {
                    value = value.add(new BigDecimal(dividend).divide(
                        new BigDecimal(divisor), 3, RoundingMode.HALF_EVEN));
                }
            } else if (decimal != null) {
                // 不带分数数值
                value = new BigDecimal(decimal);
            }

            if (groupMatcher.group(10) == null) {
                groupUnit = unit.getDisplayName();
            } else {
                groupUnit = groupMatcher.group(10);
            }

            if (groupUnit != null) {
                groupUnit = UNITS.get(groupUnit.toLowerCase());
                equivalence = BigDecimal.valueOf(EQUIVALENCES.get(groupUnit));
                mm = mm.add(value.multiply(equivalence), MathContext.DECIMAL64);
            }

        }

        if (value == null) {
            return;
        }

        if (groupUnit == null) {
            groupUnit = unit.name();
        }

        if (groupUnit != null
            && (groupUnit = UNITS.get(groupUnit.toLowerCase())) != null) {
            equivalence = BigDecimal.valueOf(EQUIVALENCES.get(groupUnit));
            value = mm.divide(equivalence, MathContext.DECIMAL64);
        }

        // 单位暂时指定为传进来的值：对于nps存储inch值，对于长度存储mm值 TODO
        this.value = value.doubleValue();
        this.unit = LengthUnit.getByName(groupUnit);
        this.millimeters = mm.doubleValue();
    }

    /**
     * 构造函数。
     *
     * @param millimeters 毫米值
     * @param unit        单位
     */
    public LengthDTO(Double millimeters, LengthUnit unit) {
        String lengthText = "";
        String groupUnit = UNITS.get(unit.toString().toLowerCase());
        BigDecimal equivalence = BigDecimal.valueOf(EQUIVALENCES.get(groupUnit));
        BigDecimal value = BigDecimal.ZERO;

        switch (unit) {
            case INCH:
                BigDecimal equivalenceFoot = BigDecimal.valueOf(EQUIVALENCES.get(UNITS.get(LengthUnit.FOOT.toString().toLowerCase())));
                BigDecimal conversion = equivalenceFoot.divide(equivalence, 0, RoundingMode.CEILING);

                value = BigDecimal.valueOf(millimeters).divide(equivalence, 3, RoundingMode.CEILING);

                BigDecimal valueFoot = value.divide(conversion, 0, RoundingMode.FLOOR);
                BigDecimal valueInch = value.remainder(conversion);

                if (!valueFoot.equals(BigDecimal.ZERO)) {
                    lengthText = valueFoot.toString() + LengthUnit.FOOT.toString();
                }
                lengthText += valueInch.toString() + unit.toString();
                break;
            case CENTIMETER:
                value = BigDecimal.valueOf(millimeters).divide(equivalence, 3, RoundingMode.CEILING);
                lengthText = value + unit.toString();
                break;
            case MILLIMETER:
                lengthText = millimeters.toString() + unit.toString();
                break;
            default:
                value = BigDecimal.valueOf(millimeters).divide(equivalence, 3, RoundingMode.CEILING);
                lengthText = value + unit.toString();
                break;
        }

        this.lengthText = lengthText;
    }

    public Double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }

    public Double getMillimeters() {
        return millimeters;
    }

    public String getLengthText() {
        return lengthText;
    }

}
