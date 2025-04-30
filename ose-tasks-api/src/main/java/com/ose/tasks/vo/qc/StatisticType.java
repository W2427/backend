package com.ose.tasks.vo.qc;

import com.ose.vo.ValueObject;

public enum StatisticType implements ValueObject {

    SMT("结构材料追踪报告", "02_02_STRUCTURE_MATERIAL_TRACEABILITY_REPORT"),
    SMR("结构材料入库报告模板", "02_01_STRUCTURE_MATERIAL_RECEIVE"),

    RT("RT报告模板", "02_09_RADIOGRAPHIC_TEST_REPORT"),
    UT("UT报告模板", "02_10_ULTRASONIC_TEST_REPORT"),
    MT("MT报告模板", "02_11_MAGNETIC_PARTICLE_TEST_REPORT"),
    PT("PT报告模板", "02_12_PENETRATION_INSPECTION_REPORT"),
    FIT_UP("管理项目的权限", "02_03_DAILY_FIT_UP_RECORD"),
    WELD("焊接", "02_09_WELDING"),
    PIPE_FITTING("管件", "02_10_PIPE_FITTING"),
    PIPE_MATERIAL("管材", "02_11_PIPE_MATERIAL"),
    VALVE_MATERIAL("阀件", "02_12_VAVLE_MATERIAL"),
    DAILY_WELDING("实体层级树只读", "02_07_DAILY_WELDING_RECORD"),
    DAILY_PRE_HEAT("实体数据导入", "02_04_DAILY_PRE_HEAT_TEMPERATURE_RECORD"),
    FIT_UP_WELDING("图纸列表只读", "02_13_STRUCTURAL_FIT_UP&_WELD_VISUAL_INSPECTION_REPORT"),
    WELDING_PARAMETERS("管线实体只读", "02_05_WELDING_PARAMETERS_VERIFICATION"),
    PRIMARY_ASSEMBLY_SUMMARY("单管实体只读", "02_14_PRIMARY_STRUCTURES_ASSEMBLY_SUMMARY_REPORT"),
    TERTIARY_STEEL("管段实体只读", "02_16_TERTIARY_STEEL_STRUCTURES_GRATINGS_CHEKERED_PLATES_ERECTION_SUMMARY_REPORT"),
    SDC("焊口实体只读", "02_08_STRUCTURE_DIMENTIONAL_CONTROL_RECORD"),
    DCF("下料二维码只读", "02_15_DIMENSIONAL_CONTROL_REPORT_FOR_SECONDARY_STRUCTURE");

    private String displayName;
    private String reportType;

    StatisticType(String displayName, String reportType) {
        this.displayName = displayName;
        this.reportType = reportType;
    }

    public String getDisplayName() {
        return displayName;
    }


    public String getReportType() {
        return reportType;
    }
}
