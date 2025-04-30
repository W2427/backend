package com.ose.report.dto.moduleProcess;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class ModuleProcessModuleAssembledDetailItemDTO extends BaseDTO {

    private static final long serialVersionUID = 8242833049206941517L;

    @Schema(description = "材料类型")
    private String materialType;

    @Schema(description = "设计数量")
    private Double iffWeight;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "完成百分比")
    private String percentage;

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public Double getIffWeight() {
        return iffWeight;
    }

    public void setIffWeight(Double iffWeight) {
        this.iffWeight = iffWeight;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
