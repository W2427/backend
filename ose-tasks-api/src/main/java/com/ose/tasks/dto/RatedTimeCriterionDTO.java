package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class RatedTimeCriterionDTO extends BaseDTO {

    private static final long serialVersionUID = 2612615597220087699L;

    @Schema(description = "是否包含米单位")
    private Boolean unitM = false;

    @Schema(description = "是否包含个单位")
    private Boolean unitN = false;

    @Schema(description = "是否包含直径")
    private Boolean pipeDiameter = false;

    @Schema(description = "是否包含管子厚度")
    private Boolean pipeThickness = false;

    @Schema(description = "是否包含材料")
    private Boolean material = false;

    @Schema(description = "是否包含测试压力")
    private Boolean testPressure = false;

    @Schema(description = "是否包含介质")
    private Boolean medium = false;

    @Schema(description = "是否包含清洁介质")
    private Boolean cleaningMedium = false;

    public Boolean getUnitM() {
        return unitM;
    }

    public void setUnitM(Boolean unitM) {
        this.unitM = unitM;
    }

    public Boolean getUnitN() {
        return unitN;
    }

    public void setUnitN(Boolean unitN) {
        this.unitN = unitN;
    }

    public Boolean getPipeDiameter() {
        return pipeDiameter;
    }

    public void setPipeDiameter(Boolean pipeDiameter) {
        this.pipeDiameter = pipeDiameter;
    }

    public Boolean getPipeThickness() {
        return pipeThickness;
    }

    public void setPipeThickness(Boolean pipeThickness) {
        this.pipeThickness = pipeThickness;
    }

    public Boolean getMaterial() {
        return material;
    }

    public void setMaterial(Boolean material) {
        this.material = material;
    }

    public Boolean getTestPressure() {
        return testPressure;
    }

    public void setTestPressure(Boolean testPressure) {
        this.testPressure = testPressure;
    }

    public Boolean getMedium() {
        return medium;
    }

    public void setMedium(Boolean medium) {
        this.medium = medium;
    }

    public Boolean getCleaningMedium() {
        return cleaningMedium;
    }

    public void setCleaningMedium(Boolean cleaningMedium) {
        this.cleaningMedium = cleaningMedium;
    }
}
