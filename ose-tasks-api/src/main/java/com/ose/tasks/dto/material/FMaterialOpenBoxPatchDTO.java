package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 更新开箱检验清单DTO
 */
public class FMaterialOpenBoxPatchDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "开箱检验报告的描述")
    private String reportDesc;

    @Schema(description = "临时文件")
    private String temporaryFileName;

    @Schema(description = "报告状态，内检通过/有部分缺损/整体质量问题")
    private String openBoxInspectionResult;

    public String getReportDesc() {
        return reportDesc;
    }

    public void setReportDesc(String reportDesc) {
        this.reportDesc = reportDesc;
    }

    public String getTemporaryFileName() {
        return temporaryFileName;
    }

    public void setTemporaryFileName(String temporaryFileName) {
        this.temporaryFileName = temporaryFileName;
    }

    public String getOpenBoxInspectionResult() {
        return openBoxInspectionResult;
    }

    public void setOpenBoxInspectionResult(String openBoxInspectionResult) {
        this.openBoxInspectionResult = openBoxInspectionResult;
    }

}
