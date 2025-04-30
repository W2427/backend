package com.ose.materialspm.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * POH list查询DTO
 */
public class FadListDTO extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "SPM 项目ID")
    private String spmProjId = "";

    @Schema(description = "领料单ID")
    private String fahId = "";

    public String getFahId() {
        return fahId;
    }

    public void setFahId(String fahId) {
        this.fahId = fahId;
    }

    public String getSpmProjId() {
        return spmProjId;
    }

    public void setSpmProjId(String spmProjId) {
        this.spmProjId = spmProjId;
    }

}
