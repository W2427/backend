package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class FMaterialStructureNestImportDTO extends BaseDTO {

    private static final long serialVersionUID = -8238899804357411153L;

    @Schema(description = "上传的导入文件的临时文件名")
    private String fileName;

    @Schema(description = "备注")
    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
