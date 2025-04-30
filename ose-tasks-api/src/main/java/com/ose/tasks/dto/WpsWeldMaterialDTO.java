package com.ose.tasks.dto;


import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class WpsWeldMaterialDTO extends BaseDTO {

    private static final long serialVersionUID = -7221910545846347002L;

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "wps编号")
    private String wpsNo;

    @Schema(description = "焊材类型")
    private String weldMaterialType;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getWpsNo() {
        return wpsNo;
    }

    public void setWpsNo(String wpsNo) {
        this.wpsNo = wpsNo;
    }

    public String getWeldMaterialType() {
        return weldMaterialType;
    }

    public void setWeldMaterialType(String weldMaterialType) {
        this.weldMaterialType = weldMaterialType;
    }
}
