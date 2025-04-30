package com.ose.material.vo;

import com.ose.util.StringUtils;
import com.ose.vo.ValueObject;

/**
 * 材料类型。
 */
public enum QrCodeType implements ValueObject {

    // 一物一码
    GOODS("一物一码", "一物一码"),

    // 一类一码
    TYPE("一类一码", "一类一码"),
    // 一类一码
    EMPTY("无", "无");


    private String displayName;

    private String description;


    QrCodeType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public static QrCodeType getByDisplayName(String displayName) {

        if (StringUtils.isEmpty(displayName)) {
            return null;
        }

        for (QrCodeType type : QrCodeType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return null;
    }

}
