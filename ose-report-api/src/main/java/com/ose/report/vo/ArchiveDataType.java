package com.ose.report.vo;

import com.ose.report.entity.statistics.*;
import com.ose.vo.ValueObject;

/**
 * 统计数据归档类型。
 */
public enum ArchiveDataType implements ValueObject {

    WBS_PROGRESS("WBS 计划进度", WBSProgressArchiveData.class, "archive_wbs_progress_of_project"),
    WBS_PROGRESS_GROUP("WBS 汇总 计划进度", WBSProgressGroupArchiveData.class, "archive_wbs_group_progress_of_project"),
    WBS_WELD_PROGRESS("焊接完成量", WBSWeldProgressArchiveData.class, "archive_build_welds_of_project"),
    WBS_FIT_UP_PROGRESS("下料完成量", WBSPipePieceProgressArchiveData.class, "archive_build_pipe_pieces_of_project"),
    WBS_SPOOL_PROGRESS("单管施工完成量", WBSProgressArchiveData.class, "archive_build_spools_of_project"),
    WBS_ISO_PROGRESS("管线使用完成量", WBSProgressArchiveData.class, "archive_build_iso"), // TODO
    WBS_PRESSURE_TEST_PROGRESS("试压进度", WBSISOProcessArchiveData.class, "archive_build_iso_ptp_of_project"),
    WBS_CLEAN_PROGRESS("清洁进度", WBSISOProcessArchiveData.class, "archive_build_iso_clean_of_project"),
    WBS_TIGHTNESS_PROGRESS("系统密性进度", WBSISOProcessArchiveData.class, "archive_build_iso_tightness_of_project"),
    WBS_INSULATION_PROGRESS("保温保冷进度", WBSISOProcessArchiveData.class, "archive_build_iso_insulation_of_project"),
    WBS_ISSUE_PROGRESS("遗留问题处理量", WBSIssuesArchiveData.class, "NO_PROCEDURE"),
    WBS_WELDER_RATE("焊工合格率", WBSWelderArchiveData.class, "archive_welder_of_project"),
    WBS_PASS_RATE_PROGRESS("报验合格率", WBSPassRateArchiveData.class, "archive_pass_rate_of_project"),
    WBS_ACTIVITIES_PROGRESS("任务数统计", WBSActivitiesArchiveData.class, "archive_activities_of_project"),
    WBS_WELD_RATE_PROGRESS("焊接合格率", WBSWeldRateArchiveData.class, "archive_weld_rate_of_project"),
    WBS_NDT_RATE_PROGRESS("NDT合格率", WBSNDTRateArchiveData.class, "archive_ndt_rate_of_project"),
    WBS_WELD("焊接数据统计", WBSWeldArchiveData.class, "archive_weld_of_project"),
    WBS_QUANTITY("建造完成量统计", WBSQuantityArchiveData.class, "archive_wbs_quantity_of_project"),
    WBS_CUTTING("下料完成量统计", WBSCuttingArchiveData.class, "archive_wbs_cutting_of_project"),
    WBS_SPOOL("单管施工完成量统计", WBSSpoolArchiveData.class, "archive_wbs_spool_of_project"),
    WBS_ISO("管线施工完成量统计", WBSSpoolArchiveData.class, "archive_wbs_iso_of_project"),
    DETAIL_DESIGN("生产设计详细设计状态统计", DetailDesignArchiveData.class, "archive_detail_design_of_project"),
    DESIGN_RATE("生产设计完成率统计", DesignRateArchiveData.class, "archive_design_rate_of_project"),
    DESIGN_HOUR("生产设计工时统计", DesignHourArchiveData.class, "archive_design_hour_of_project"),
    WBS_PRESSURE_TEST("试压进度统计", WBSSpoolArchiveData.class, "archive_wbs_pressure_test_of_project"),
    WBS_CLEAN_PACKAGE("清洁进度统计", WBSSpoolArchiveData.class, "archive_wbs_clean_package_of_project"),
    WBS_SUB_SYSTEM("系统密性进度统计", WBSSpoolArchiveData.class, "archive_wbs_sub_system_of_project"),

    MAN_HOUR_STATISTICS("工时进度统计", ManHourStatisticsArchiveData.class, "man_hour_statistics"),
    DIVISION_STATISTICS("部门工时进度统计", DivisionStatisticsArchiveData.class, "division_statistics"),
    MAN_HOUR_POWER_STATISTICS("部门工时人力统计", ManHourPowerStatisticsArchiveData.class, "man_hour_power_statistics"),



    WBS_TAG_WEIGHT_RATE_PROGRESS("Tag重量完成统计", WBSTagWeightRateArchiveData.class, "archive_tag_weight_rate_of_project");


    // 表示名称
    private String displayName;

    // 数据实体类型
    private Class<? extends ArchiveDataBase> type;

    // 归档存储过程名称
    private String storedProcedureName;

    ArchiveDataType(
        String displayName,
        Class<? extends ArchiveDataBase> type,
        String storedProcedureName
    ) {
        this.displayName = displayName;
        this.type = type;
        this.storedProcedureName = storedProcedureName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public Class<? extends ArchiveDataBase> getType() {
        return type;
    }

    public String getStoredProcedureName() {
        return storedProcedureName;
    }

}
