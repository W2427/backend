package com.ose.tasks.vo.workinghour;

import com.ose.vo.ValueObject;

/**
 * 项目工时分类类型。
 */
public enum ProjectWorkingHourCategoryType implements ValueObject {

    LARGE_CATEGORY("大分类", "大分类"),

    SMALL_CATEGORY("小分类", "小分类");


    private String displayName;

    private String description;


    ProjectWorkingHourCategoryType(String displayName, String description) {
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
