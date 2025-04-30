package com.ose.materialspm.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Demo查询DTO
 */
public class BomNodeDTO extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "SPM 项目ID")
    private String spmProjId = "";

    @Schema(description = "SPM BOM结构")
    private String bompath = "";

    public String getSpmProjId() {
        return spmProjId;
    }

    public void setSpmProjId(String spmProjId) {
        this.spmProjId = spmProjId;
    }

    public String getBompath() {
        return bompath;
    }

    public void setBompath(String bompath) {
        this.bompath = bompath;
    }

}
