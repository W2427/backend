package com.ose.materialspm.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * POH list查询DTO
 */
public class ReleaseNoteDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "SPM 项目ID")
    private String spmProjId = "";

    @Schema(description = "SPM 放行单号")
    private String relnNumber = "";

    public String getSpmProjId() {
        return spmProjId;
    }

    public void setSpmProjId(String spmProjId) {
        this.spmProjId = spmProjId;
    }

    public String getRelnNumber() {
        return relnNumber;
    }

    public void setRelnNumber(String relnNumber) {
        this.relnNumber = relnNumber;
    }

}
