package com.ose.vo.unit;

import com.ose.vo.ValueObject;
import com.ose.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 温度单位。
 */
public enum TemperatureUnit implements ValueObject {

    // 摄氏
    C("°C", "摄氏度", "c", "°c", "℃"),

    // 华氏
    F("°F", "华氏度", "f", "°f"),

    // 开尔文
    K("K", "开尔文", "k");

    private String displayName;

    private String description;

    private List<String> names;

    TemperatureUnit(String displayName, String description, String... names) {
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

    public static TemperatureUnit getByName(String name) {

        if (StringUtils.isEmpty(name)) {
            return null;
        }

        name = name.toLowerCase().replaceAll("[^0-9a-zA-Z]+", "");

        for (TemperatureUnit unit : TemperatureUnit.values()) {
            if (unit.names.indexOf(name) >= 0) {
                return unit;
            }
        }

        return null;
    }

}
