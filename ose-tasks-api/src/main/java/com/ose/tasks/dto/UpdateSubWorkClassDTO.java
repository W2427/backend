package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class UpdateSubWorkClassDTO extends BaseDTO {

    @Schema(description = "工作效率")
    private String productivity;

    @Schema(description = "目标重量")
    private String targetQty;

    @Schema(description = "目标工时")
    private String targetManHours;

    @Schema(description = "权重")
    private String weightAge;

    public String getProductivity() {
        return productivity;
    }

    public void setProductivity(String productivity) {
        this.productivity = productivity;
    }

    public String getTargetQty() {
        return targetQty;
    }

    public void setTargetQty(String targetQty) {
        this.targetQty = targetQty;
    }

    public String getTargetManHours() {
        return targetManHours;
    }

    public void setTargetManHours(String targetManHours) {
        this.targetManHours = targetManHours;
    }

    public String getWeightAge() {
        return weightAge;
    }

    public void setWeightAge(String weightAge) {
        this.weightAge = weightAge;
    }
}
