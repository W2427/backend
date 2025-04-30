package com.ose.tasks.vo.workinghour;

import com.ose.vo.ValueObject;

/**
 * 项目工时状态类型。
 */
public enum ProjectWorkingHourStatusType implements ValueObject {

    NOT_SUBMITTED("未提交", "未提交"),

    SUBMITTED("已提交", "已提交"),

    WITHDRAW("撤回", "撤回"),

    REJECTED("驳回", "驳回"),

    APPROVED("批准", "批准"),

    DELETED("删除", "删除");

    private String displayName;

    private String description;


    ProjectWorkingHourStatusType(String displayName, String description) {
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

}
