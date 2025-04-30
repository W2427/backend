package com.ose.tasks.dto.numeric;

import com.ose.tasks.vo.PressureExtraInfo;
import com.ose.util.StringUtils;
import com.ose.vo.unit.PressureUnit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 压力值对象。
 */
public class PressureDTO {

    private static final Pattern PRESSURE_PATTERN = Pattern.compile(
        "(\\d+(\\.\\d+)?)\\s*(PSI|bar|kpa|Mpa)?",
        Pattern.CASE_INSENSITIVE
    );

    private static final Pattern EXTRA_INFO_PATTERN = Pattern.compile(
        "(^|[^\\d\\w])(FV|ATM)([^\\d\\w]|$)",
        Pattern.CASE_INSENSITIVE
    );

    // 压力值
    private Double value = null;

    // 压力单位
    private PressureUnit unit = null;

    // 压力附加信息
    private PressureExtraInfo extraInfo = null;

    /**
     * 构造方法。
     */
    public PressureDTO(PressureUnit unit, String string) {

        Matcher matcher;
        String group;
        Matcher groupMatcher;
        if(StringUtils.isEmpty(string)) {
            this.unit = unit;
            value = 0.0;
            return;
        }
        matcher = PRESSURE_PATTERN.matcher(string);

        if (matcher.find()) {

            groupMatcher = PRESSURE_PATTERN.matcher(matcher.group());

            if (groupMatcher.matches()) {

                value = Double.valueOf(groupMatcher.group(1));

                if (groupMatcher.group(3) != null) {
                    this.unit = PressureUnit.getByName(groupMatcher.group(3));
                }

            }

        }

        matcher = EXTRA_INFO_PATTERN.matcher(string);

        if (matcher.find()) {

            groupMatcher = EXTRA_INFO_PATTERN.matcher(matcher.group());

            if (groupMatcher.matches()) {
                extraInfo = PressureExtraInfo.valueOf(
                    matcher.group(2).toUpperCase()
                );
            }

        }

        if (this.unit == null) {
            this.unit = unit;
        }

        if (value == null && extraInfo == PressureExtraInfo.ATM) {
            value = 0.0;
        }

    }

    public Double getValue() {
        return value;
    }

    public PressureUnit getUnit() {
        return unit;
    }

    public PressureExtraInfo getExtraInfo() {
        return extraInfo;
    }

}
