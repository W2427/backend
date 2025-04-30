package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 采购包文件创建DTO
 */
public class MmPurchasePackageFileCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -2970183985952445955L;

    @Schema(description = "上传的导入文件的临时文件名")
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
