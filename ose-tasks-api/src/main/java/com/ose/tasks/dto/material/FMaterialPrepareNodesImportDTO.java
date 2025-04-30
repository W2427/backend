package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class FMaterialPrepareNodesImportDTO extends BaseDTO {

    private static final long serialVersionUID = -4844502779654639331L;

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
