package com.ose.tasks.vo.qc;

import com.ose.vo.ValueObject;

public enum ReportType implements ValueObject {

    DESIGN("设计", "DESIGN"),
    MATERIAL("材料", "MATERIAL"),
    STRUCT_CONSTRUCT("结构建造", "STRUCT_CONSTRUCT"),
    PIPING_CONSTRUCT("管系建造", "PIPING_CONSTRUCT");

    private String displayName;
    private String reportType;

    ReportType(String displayName, String reportType) {
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
