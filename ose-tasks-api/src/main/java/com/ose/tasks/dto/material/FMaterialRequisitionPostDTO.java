package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 领料单查询DTO
 */
public class FMaterialRequisitionPostDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "SPM 领料单ID")
    private String spmFahId;

    public String getSpmFahId() {
        return spmFahId;
    }

    public void setSpmFahId(String spmFahId) {
        this.spmFahId = spmFahId;
    }
}
