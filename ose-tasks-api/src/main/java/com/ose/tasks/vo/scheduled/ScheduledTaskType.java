package com.ose.tasks.vo.scheduled;

import com.ose.vo.ValueObject;

/**
 * 定时任务类型。
 */
public enum ScheduledTaskType implements ValueObject {

    STORED_PROCEDURE("关系型数据库存储过程"),
    SPRING_FRAMEWORK("Spring 框架定时任务");

    private String displayName;

    ScheduledTaskType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
