package com.ose.tasks.vo.qc;

import com.ose.vo.ValueObject;

/**
 * 验收报告类型。
 */
public enum ReportSubType implements ValueObject {
    PIPE_MATERIAL(
        "03_11_PIPE_MATERIAL_INSPECTION_REPORT",
        "南通管材料验收报告(旧)",
        "MATERIAL"),
    PIPE_FITTING(
        "03_02_PIPE_FITTING_INSPECTION_REPORT",
        "南通管附件验收报告(旧)",
        "MATERIAL"),
    VALVE(
        "03_03_VALVE_INSPECTION_REPORT",
        "南通阀件验收报告(旧)",
        "MATERIAL"),
    PIPE_NT_F253_MATERIAL(
        "F253xPipeMaterialInspectionReport",
        "南通管材料验收报告(新)",
        "MATERIAL"),
    PIPE_NT_F253_FITTING(
        "F253xPipeFittingInspectionReport",
        "南通管附件验收报告(新)",
        "MATERIAL"),
    VALVE_NT_F253(
        "F253xPipeValvesInspectionReport",
        "南通阀件验收报告(新)",
        "MATERIAL"),
    STRUCTURE_MATERIAL(
        "02_01_STRUCTURE_MATERIAL_INSPECTION_RECORD",
        "F217结构材料验收报告",
        "MATERIAL"),
    STRUCTURE_MATERIAL_TRACEABILITY_REPORT(
        "02_02_STRUCTURE_MATERIAL_TRACEABILITY_REPORT",
        "结构材料追踪报告",
        "STRUCT_CONSTRUCT"),
    DAILY_FIT_UP_RECORD(
        "02_03_DAILY_FIT_UP_RECORD",
        "焊前检验报告单",
        "STRUCT_CONSTRUCT"),
    DAILY_PRE_HEAT_TEMPERATURE_RECORD(
        "02_04_DAILY_PRE_HEAT_TEMPERATURE_RECORD",
        "预热温度记录",
        "STRUCT_CONSTRUCT"),
    WELDING_PARAMETERS_VERIFICATION(
        "02_05_WELDING_PARAMETERS_VERIFICATION",
        "焊接参数记录",
        "STRUCT_CONSTRUCT"),
    DAILY_WELDING_RECORD(
        "02_07_DAILY_WELDING_RECORD",
        "焊后外观检验报告",
        "STRUCT_CONSTRUCT"),
    STRUCTURE_DIMENTIONAL_CONTROL_RECORD(
        "02_08_STRUCTURE_DIMENTIONAL_CONTROL_RECORD",
        "焊后尺寸控制报告",
        "STRUCT_CONSTRUCT"),
    RADIOGRAPHIC_TEST_REPORT(
        "02_09_RADIOGRAPHIC_TEST_REPORT",
        "RT报告模板",
        "STRUCT_CONSTRUCT"),
    ULTRASONIC_TEST_REPORT(
        "02_10_ULTRASONIC_TEST_REPORT",
        "UT报告模板",
        "STRUCT_CONSTRUCT"),
    MAGNETIC_PARTICLE_TEST_REPORT(
        "02_11_MAGNETIC_PARTICLE_TEST_REPORT",
        "MT报告模板",
        "STRUCT_CONSTRUCT"),
    PENETRATION_INSPECTION_REPORT(
        "02_12_PENETRATION_INSPECTION_REPORT",
        "PT报告模板",
        "STRUCT_CONSTRUCT"),
    STRUCTURAL_FIT_UP_WELD_VISUAL_INSPECTION_REPORT(
        "02_13_STRUCTURAL_FIT_UP&_WELD_VISUAL_INSPECTION_REPORT",
        "1800-ST02结构装配和焊接外观检验报告",
        "STRUCT_CONSTRUCT"),
    PRIMARY_STRUCTURES_ASSEMBLY_SUMMARY_REPORT(
        "02_14_PRIMARY_STRUCTURES_ASSEMBLY_SUMMARY_REPORT",
        "1800-CS02主结构组立总结报告",
        "STRUCT_CONSTRUCT"),
    DIMENSIONAL_CONTROL_REPORT_FOR_SECONDARY_STRUCTURE(
        "02_15_DIMENSIONAL_CONTROL_REPORT_FOR_SECONDARY_STRUCTURE",
        "二类结构尺寸控制报告",
        "STRUCT_CONSTRUCT"),
    TERTIARY_STEEL_STRUCTURES_GRATINGS_CHEKERED_PLATES_ERECTION_SUMMARY_REPORT(
        "02_16_TERTIARY_STEEL_STRUCTURES_GRATINGS_CHEKERED_PLATES_ERECTION_SUMMARY_REPORT",
        "1800-CS02A三类结构搭载总结报告",
        "STRUCT_CONSTRUCT"),

    // 南通项目报告类型 --2021/01/06  chenqiang--
    STRUCTURE_NT_F253_FIT_UP(
        "F253xStructureFabricationFitupInspectionReport",
        "F253结构组对报告",
        "STRUCT_CONSTRUCT"
    ),
    STRUCTURE_NT_F253_WELD(
        "F253xStructureFabricationFinalInspectionReport",
        "F253结构焊接报告",
        "STRUCT_CONSTRUCT"
    ),
    STRUCTURE_NT_F253_MATERIAL(
        "F253xStructureMaterialInspectionReport",
        "F253结构材料验收报告",
        "MATERIAL"
    ),
    PIPING_NT_F253_WELD(
        "F253xPipingAfterWeldingInspectionReport",
        "F253管系焊后报告",
        "PIPING_CONSTRUCT"
    ),
    PIPING_NT_F253_FIT_UP(
        "F253xPipingFitUpInspectionReport",
        "F253管系组对报告",
        "PIPING_CONSTRUCT"
    ),

    PIPING_NT_F253_Magnetic_Particle(
        "F253xPipingMagneticParticleInspectionReport",
        "南通管系磁粉探伤报告模板",
        "PIPING_CONSTRUCT"),
    PIPING_NT_F253_Radio_Graphic(
        "F253xPipingRadiographicInspectionReport",
        "南通管系射线探伤报告模板",
        "PIPING_CONSTRUCT"),
    PIPING_NT_F253_Ultrasonic(
        "F253xPipingUltrasonicInspectionReport",
        "南通管系超声探伤报告模板",
        "PIPING_CONSTRUCT"),
    PIPING_NT_F253_Penetrant(
        "F253xPipingPenetrantInspectionReport",
        "南通管系渗透探伤报告模板",
        "PIPING_CONSTRUCT"),


    STRUCTURE_NT_F253_Radio_Graphic(
        "F253xStructureRadiographicInspectionReport",
        "南通结构射线探伤报告模板",
        "STRUCT_CONSTRUCT"),

    STRUCTURE_NT_F253_Ultrasonic(
        "F253xStructurexUltrasonicInspectionReport",
        "南通结构超声探伤报告模板",
        "STRUCT_CONSTRUCT"),

    STRUCTURE_NT_F253_Magnetic_Particle(
        "F253xStructureMagneticParticleInspectionReport",
        "南通结构磁粉探伤报告模板",
        "STRUCT_CONSTRUCT"),

    STRUCTURE_NT_F253_Penetrant(
        "F253xStructurePenetrantInspectionReport",
        "南通结构渗透探伤报告模板",
        "STRUCT_CONSTRUCT"),


    PIPING_MATERIAL(
        "F217xPipingx91xPipingMaterialReceiveInspectionReport",
        "F217管材验收报告",
        "MATERIAL"),

    PIPELINE_FIT_UP(
        "pipeline-fit-up-inspection/report",
        "管系装配报告(旧)",
        "PIPING_CONSTRUCT"),

    PIPING_FIT_UP(
        "F217xPipingx07xPipingFitupInspectionReport",
        "管系装配报告",
        "PIPING_CONSTRUCT"),

    PIPING_PRE_FABRICATION(
        "final-inspection-record-for-piping-pre-fabrication",
        "管系焊接报告(旧)",
        "PIPING_CONSTRUCT"),

    PIPING_WELD(
        "F217xPipingx08xPipingVisualInspectionReport",
        "管系焊接报告",
        "PIPING_CONSTRUCT"),

    PIPING_SPOOL_RELEASE(
        "F217xPipingx23xSpoolReleaseInspectionReport",
        "管段放行报告",
        "PIPING_CONSTRUCT"),

    PIPING_Radio_Graphic(
        "F217xPipingx10xRadiographicTestReport",
        "F217射线探伤报告模板",
        "PIPING_CONSTRUCT"),

    PIPING_Ultrasonic(
        "F217xPipingx11xUltrasonicTestReport",
        "F217超声探伤报告模板",
        "PIPING_CONSTRUCT"),

    PIPING_Magnetic_Particle(
        "F217xPipingx12xMagneticParticleTestReport",
        "F217磁粉探伤报告模板",
        "PIPING_CONSTRUCT"),

    PIPING_Penetration(
        "F217xPipingx13xPenetrationTestReport",
            "F217渗透探伤报告模板",
            "PIPING_CONSTRUCT"),

    PIPING_PWHT(
        "F217xPipingx14xPipingPWHTandHDTestReport",
        "F217焊后热处理报告模板",
        "PIPING_CONSTRUCT"),

    PIPING_PMI(
        "F217xPipingx22xPMITestReport",
        "F217材质分析报告模板",
        "PIPING_CONSTRUCT"),

    STRUCTURE_FIT_UP(
        "F217xStructurex07xStructureFitupInspectionReport",
        "结构装配报告",
        "STRUCT_CONSTRUCT"),

    STRUCTURE_ST02(
        "F217xStructureST02FitupInspectionReport",
        "结构ST报告",
        "STRUCT_CONSTRUCT"),

    STRUCTURE_CS02(
        "F217xStructureCS02FitupInspectionReport",
        "结构CS报告",
        "STRUCT_CONSTRUCT"),

    STRUCTURE_WELD(
        "F217xStructurex08xStructureVisualInspectionReport",
        "结构焊接报告",
        "STRUCT_CONSTRUCT"),

    STRUCTURE_Radio_Graphic(
        "F217xStructurex10xRadiographicTestReport",
        "结构射线探伤报告模板",
        "STRUCT_CONSTRUCT"),

    STRUCTURE_Phase_Array_Ultrasonic(
        "F217xStructurex10xRhaseArrayUltrasonicTestReport",
        "结构PAUT报告模板",
        "STRUCT_CONSTRUCT"),

    STRUCTURE_Ultrasonic(
        "F217xStructurex11xUltrasonicTestReport",
        "结构超声探伤报告模板",
        "STRUCT_CONSTRUCT"),

    STRUCTURE_Magnetic_Particle(
        "F217xStructurex12xMagneticParticleTestReport",
        "结构磁粉探伤报告模板",
        "STRUCT_CONSTRUCT"),

    STRUCTURE_Penetration(
        "F217xStructurex13xPenetrationTestReport",
            "结构渗透探伤报告模板",
            "STRUCT_CONSTRUCT");


    // 报告类型
    private String reportType;

    // 模版名
    private String templateName;

    // 中文名
    private String displayName;

    ReportSubType(String templateName, String displayName, String reportType) {
        this.reportType = reportType;
        this.templateName = templateName;
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    public String getReportType() {
        return this.reportType;
    }

    public String getTemplateName() {
        return this.templateName;
    }

}
