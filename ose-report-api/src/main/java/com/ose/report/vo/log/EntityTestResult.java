package com.ose.report.vo.log;

import com.ose.vo.ValueObject;

/**
 * 实体检查结果。
 */
public enum EntityTestResult implements ValueObject {

    UNFINISHED("未完成"),
    FINISHED("完成");

    private String displayName;

    EntityTestResult(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
