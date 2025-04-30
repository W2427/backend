package com.ose.report.vo;

import com.ose.vo.ValueObject;

/**
 * 数据统计计划任务类型。
 */
public enum ArchiveScheduleType implements ValueObject {

    DAILY("日次"),
    WEEKLY("周次"),
    MONTHLY("月次"),
    ANNUALLY("年次"),
    CUSTOM_REPORT("定制报表");

    private String displayName;

    ArchiveScheduleType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
