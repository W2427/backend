package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class StructureNtF253UltrasonicTestInspectionReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -53912898654701704L;

    @Schema(name = "焊口号")
    private String weldNo;

    @Schema(name = "焊工号")
    private String welderNo;

    @Schema(name = "焊接长度")
    private String weldLength;

    @Schema(name = "焊口类型")
    private String weldType;

    public String getWeldNo() {
        return weldNo;
    }

    public void setWeldNo(String weldNo) {
        this.weldNo = weldNo;
    }

    public String getWelderNo() {
        return welderNo;
    }

    public void setWelderNo(String welderNo) {
        this.welderNo = welderNo;
    }

    public String getWeldLength() {
        return weldLength;
    }

    public void setWeldLength(String weldLength) {
        this.weldLength = weldLength;
    }

    public String getWeldType() {
        return weldType;
    }

    public void setWeldType(String weldType) {
        this.weldType = weldType;
    }
}
