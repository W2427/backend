package com.ose.tasks.dto.material;

import com.ose.dto.PageDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 出库清单单DTO
 */
public class FMaterialIssueReceiptSearchListDTO extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "出库清单号")
    private String fmirCode;

    public String getFmirCode() {
        return fmirCode;
    }

    public void setFmirCode(String fmirCode) {
        this.fmirCode = fmirCode;
    }

}
