package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class StructureNtF253RadiographicInspectionReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 7908191782140504186L;

    @Schema(name = "焊口号")
    private String weldNo;

    @Schema(name = "焊工号")
    private String welderNo;

    @Schema(name = "长度")
    private String weldLength;


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

}
