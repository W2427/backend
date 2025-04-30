package com.ose.notifications.vo;

import com.ose.vo.ValueObject;

/**
 * 业务代码类型值对象。
 */
public enum BizCodeType {

    NOTIFICATION_TYPE(NotificationType.values());

    private Boolean isEnum = false;

    private ValueObject[] valueObjects;

    BizCodeType() {
    }

    BizCodeType(ValueObject[] valueObjects) {
        isEnum = true;
        this.valueObjects = valueObjects;
    }

    public Boolean isEnum() {
        return isEnum;
    }

    public ValueObject[] getValueObjects() {
        return valueObjects;
    }

}
