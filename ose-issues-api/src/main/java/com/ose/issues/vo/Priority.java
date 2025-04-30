package com.ose.issues.vo;

import com.ose.vo.ValueObject;

/**
 * 遗留问题优先级。
 */
public enum Priority implements ValueObject {

    LOW("低"),
    MEDIUM("中"),
    HIGH("高");

    private String displayName;

    Priority(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 根据表示名取得问题优先级。
     *
     * @param displayName 表示名
     * @return 问题优先级
     */
    public static Priority getByDisplayName(String displayName) {

        if (displayName != null) {
            for (Priority priority : Priority.values()) {
                if (priority.displayName.equals(displayName)) {
                    return priority;
                }
            }
        }

        return null;
    }

}
