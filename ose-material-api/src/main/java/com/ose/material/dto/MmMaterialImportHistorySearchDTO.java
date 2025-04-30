package com.ose.material.dto;

import com.ose.dto.PageDTO;
import com.ose.material.vo.MaterialImportType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 材料导入历史查询查询DTO
 */
public class MmMaterialImportHistorySearchDTO extends PageDTO {

    private static final long serialVersionUID = -7189385652485245730L;

    @Schema(description = "搜索关键字")
    public String keyword;

    @Schema(description = "指定材料导入类型")
    public MaterialImportType materialImportType;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public MaterialImportType getMaterialImportType() {
        return materialImportType;
    }

    public void setMaterialImportType(MaterialImportType materialImportType) {
        this.materialImportType = materialImportType;
    }
}
