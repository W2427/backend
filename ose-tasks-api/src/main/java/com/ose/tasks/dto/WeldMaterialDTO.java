package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.WeldMaterialType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class WeldMaterialDTO extends BaseDTO {

    private static final long serialVersionUID = 3391448094625821290L;

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "焊材类型")
    @Enumerated(EnumType.STRING)
    private WeldMaterialType weldMaterialType;

    @Schema(description = "焊剂")
    private String flux;


    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getFlux() {
        return flux;
    }

    public void setFlux(String flux) {
        this.flux = flux;
    }

    public WeldMaterialType getWeldMaterialType() {
        return weldMaterialType;
    }

    public void setWeldMaterialType(WeldMaterialType weldMaterialType) {
        this.weldMaterialType = weldMaterialType;
    }
}
