package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public class ExternalInspectionDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 7313108204857929588L;

    @Schema(description = "工序")
    private String process;

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
