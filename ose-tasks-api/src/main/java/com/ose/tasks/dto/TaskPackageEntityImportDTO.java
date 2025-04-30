package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class TaskPackageEntityImportDTO extends BaseDTO {

    private static final long serialVersionUID = -8506562209480554575L;

    @Schema(description = "上传的导入文件的临时文件名")
    private String fileName;

    @Schema(description = "备注")
    private String remarks;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
