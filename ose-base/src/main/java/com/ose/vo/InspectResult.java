package com.ose.vo;

import com.ose.util.StringUtils;

/**
 * 项目条目类型。
 */
public enum InspectResult implements ValueObject {

    A(BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_NO_COMMENT.getType()),
    B(BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_COMMENT.getType()),
    C(BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_REJECT.getType());

    private String displayName;

    InspectResult(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public static InspectResult getByDisplayName(String displayName) {

        if (StringUtils.isEmpty(displayName)) {
            return null;
        }

        for (InspectResult type : InspectResult.values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return null;
    }

}
