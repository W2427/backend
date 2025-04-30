package com.ose.vo.unit;

import com.ose.vo.ValueObject;
import com.ose.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 压力单位。
 */
public enum PressureUnit implements ValueObject {

    // 磅/平方英寸
    PSI("psi", "磅/平方英寸", "psi"),
    // 巴
    BAR("bar", "ba", "bar"),
    // 千帕（绝压）
    KPAA("KPAA", "千帕（绝压）", "kpaa"),
    // 千帕（表压）
    KPAG("KPAG", "千帕（表压）", "kpag"),
    // 兆帕
    MPA("Mpa", "兆帕", "Mpa", "mpa");

    private String displayName;

    private String description;

    private List<String> names;

    PressureUnit(String displayName, String description, String... names) {
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

    public static PressureUnit getByName(String name) {

        if (StringUtils.isEmpty(name)) {
            return null;
        }

        name = name.toLowerCase().replaceAll("\\s+", "_");

        for (PressureUnit unit : PressureUnit.values()) {
            if (unit.names.indexOf(name) >= 0) {
                return unit;
            }
        }

        return null;
    }

}
