package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 材料准备单DTO
 */
public class FMaterialPreparePatchForActivityDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "SPM 领料单ID")
    private String spmFahId;

    @Schema(description = "SPM 领料单号")
    private String spmFahCode;

    @Schema(description = "SPM 运行编号")
    private String spmRunNumber;

    public String getSpmFahId() {
        return spmFahId;
    }

    public void setSpmFahId(String spmFahId) {
        this.spmFahId = spmFahId;
    }

    public String getSpmFahCode() {
        return spmFahCode;
    }

    public void setSpmFahCode(String spmFahCode) {
        this.spmFahCode = spmFahCode;
    }

    public String getSpmRunNumber() {
        return spmRunNumber;
    }

    public void setSpmRunNumber(String spmRunNumber) {
        this.spmRunNumber = spmRunNumber;
    }
}
