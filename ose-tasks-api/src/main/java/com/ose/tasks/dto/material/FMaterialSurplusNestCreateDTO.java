package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 套料单DTO
 */
public class FMaterialSurplusNestCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 2885305040816715763L;

    @Schema(description = "余料领料单编号")
    private String fMaterialSurplusReceiptsNo;

    @Schema(description = "余料领料单id")
    private Long fMaterialSurplusReceiptsId;

    @Schema(description = "描述")
    private String memo;

    public String getfMaterialSurplusReceiptsNo() {
        return fMaterialSurplusReceiptsNo;
    }

    public void setfMaterialSurplusReceiptsNo(String fMaterialSurplusReceiptsNo) {
        this.fMaterialSurplusReceiptsNo = fMaterialSurplusReceiptsNo;
    }

    public Long getfMaterialSurplusReceiptsId() {
        return fMaterialSurplusReceiptsId;
    }

    public void setfMaterialSurplusReceiptsId(Long fMaterialSurplusReceiptsId) {
        this.fMaterialSurplusReceiptsId = fMaterialSurplusReceiptsId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
