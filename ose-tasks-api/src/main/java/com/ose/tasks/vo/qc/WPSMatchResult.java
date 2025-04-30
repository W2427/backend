package com.ose.tasks.vo.qc;

import com.ose.vo.ValueObject;

/**
 * WPS匹配结果。
 */
public enum WPSMatchResult implements ValueObject {

    SUCCESS("成功"),
    FAILURE("失败"),
    SKIP("跳过");

    private String displayName;

    WPSMatchResult(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
