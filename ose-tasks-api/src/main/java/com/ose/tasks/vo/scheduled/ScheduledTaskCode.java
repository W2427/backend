package com.ose.tasks.vo.scheduled;

import com.ose.vo.ValueObject;

/**
 * 定时任务业务代码。
 */
public enum ScheduledTaskCode implements ValueObject {

    SET_HIERARCHY_NODE_PROGRESS("层级节点实体工序实施进度更新", ScheduledTaskType.STORED_PROCEDURE),
    START_ENTITY_PROCESS("实体工序工作流自动启动", ScheduledTaskType.SPRING_FRAMEWORK),
    ARCHIVE_WBS_PROGRESS("数据归档：建造计划 / 完成率，建造计划 / 工时", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_PROGRESS_GROUP("数据归档：建造计划汇总", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_WELD_PROGRESS("焊口建造完成量数据归档", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_FIT_UP_PROGRESS("下料完成量数据归档", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_SPOOL_PROGRESS("单管施工完成量数据归档", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_PRESSURE_TEST_PROGRESS("试压进度数据归档", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_CLEAN_PROGRESS("清洁进度数据归档", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_TIGHTNESS_PROGRESS("系统密性进度数据归档", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_INSULATION_PROGRESS("保温保冷进度数据归档", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_WELDER_RATE("焊工合格率数据归档", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_PASS_RATE_PROGRESS("数据归档：质量控制 / 报验合格率", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_ACTIVITIES_PROGRESS("数据归档：任务管理 / 任务统计", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_WELD_RATE_PROGRESS("数据归档：质量控制 / 焊接合格率", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_NDT_RATE_PROGRESS("数据归档：质量控制 / NDT合格率", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_WELD("数据归档：建造计划 / 焊接完成量", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_QUANTITY("数据归档：建造计划 / 建造完成量", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_CUTTING("数据归档：建造计划 / 下料完成量", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_SPOOL("数据归档：建造计划 / 单管施工完成量", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_ISO("数据归档：建造计划 / 管线施工完成量", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_DETAIL_DESIGN("数据归档：生产设计 / 详细设计状态", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_DESIGN_RATE("数据归档：生产设计 / 完成率", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_DESIGN_HOUR("数据归档：生产设计 / 工时", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_PRESSURE_TEST("数据归档：建造计划 / 试压进度", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_CLEAN_PACKAGE("数据归档：建造计划 / 清洁进度", ScheduledTaskType.STORED_PROCEDURE),
    ARCHIVE_WBS_SUB_SYSTEM("数据归档：建造计划 / 系统密性进度", ScheduledTaskType.STORED_PROCEDURE);

    private String displayName;

    private ScheduledTaskType type;

    ScheduledTaskCode(String displayName, ScheduledTaskType type) {
        this.displayName = displayName;
        this.type = type;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public ScheduledTaskType getType() {
        return type;
    }

}
