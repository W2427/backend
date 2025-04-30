package com.ose.materialspm.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExportFileInputDataDTO extends BaseDTO {

    private static final long serialVersionUID = 5971440931157335574L;

    @Schema(description = "SHEET名")
    private String sheetName;

    @Schema(description = "标题")
    private LinkedHashMap<String, String> title;

    @Schema(description = "内容列表")
    private List<Map<String, Object>> exportDataList;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public LinkedHashMap<String, String> getTitle() {
        return title;
    }

    public void setTitle(LinkedHashMap<String, String> title) {
        this.title = title;
    }

    public List<Map<String, Object>> getExportDataList() {
        return exportDataList;
    }

    public void setExportDataList(List<Map<String, Object>> exportDataList) {
        this.exportDataList = exportDataList;
    }
}
