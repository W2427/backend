package com.ose.tasks.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class ExportExcelDTO extends PageDTO {


    //ftj
    @Schema(description = "导出视图名称")
    private String exportViewName;

    @Schema(description = "导出Excel的说明")
    private String excelDesc;

    public String getExportViewName() {
        return exportViewName;
    }

    public void setExportViewName(String exportViewName) {
        this.exportViewName = exportViewName;
    }

    public String getExcelDesc() {
        return excelDesc;
    }

    public void setExcelDesc(String excelDesc) {
        this.excelDesc = excelDesc;
    }
}
