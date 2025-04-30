package com.ose.report.vo.log;

import com.ose.vo.ValueObject;

/**
 * 项目条目类型。
 */
public enum ExInspStatus implements ValueObject {

    OK("通过"),
    FAILED("不通过");

    private String displayName;

    ExInspStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
