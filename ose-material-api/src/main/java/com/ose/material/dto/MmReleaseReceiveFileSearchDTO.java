package com.ose.material.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 入库单文件查询DTO
 */
public class MmReleaseReceiveFileSearchDTO extends PageDTO {

    private static final long serialVersionUID = 412366371228213426L;
    @Schema(description = "搜索关键字")
    public String materialImportType;

    public String getMaterialImportType() {
        return materialImportType;
    }

    public void setMaterialImportType(String materialImportType) {
        this.materialImportType = materialImportType;
    }
}
