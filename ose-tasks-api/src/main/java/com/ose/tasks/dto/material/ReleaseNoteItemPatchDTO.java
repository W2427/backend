package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.qc.ReportSubType;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 更新材料验收报告DTO
 */
public class ReleaseNoteItemPatchDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "验收报告类型")
    private ReportSubType reportSubType;

    public ReportSubType getReportSubType() {
        return reportSubType;
    }

    public void setReportSubType(ReportSubType reportSubType) {
        this.reportSubType = reportSubType;
    }

}
