package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.RatedTimeUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class RatedTimeCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 7984945260384928747L;

    @Schema(description = "单位")
    @Enumerated(EnumType.STRING)
    private RatedTimeUnit unit;

    @Schema(description = "管径")
    private Double pipeDiameter;

    @Schema(description = "管子最小厚度")
    private Double minPipeThickness;

    @Schema(description = "管子最大厚度")
    private Double maxPipeThickness;

    @Schema(description = "材质")
    private String material;

    @Schema(description = "最小试压")
    private Double minTestPressure;

    @Schema(description = "最大试压")
    private Double maxTestPressure;

    @Schema(description = "介质")
    private String medium;

    @Schema(description = "清洁介质")
    private String cleaningMedium;

    @Schema(description = "人工时")
    private Double hourNorm;

    public RatedTimeUnit getUnit() {
        return unit;
    }

    public void setUnit(RatedTimeUnit unit) {
        this.unit = unit;
    }

    public Double getPipeDiameter() {
        return pipeDiameter;
    }

    public void setPipeDiameter(Double pipeDiameter) {
        this.pipeDiameter = pipeDiameter;
    }

    public Double getMinPipeThickness() {
        return minPipeThickness;
    }

    public void setMinPipeThickness(Double minPipeThickness) {
        this.minPipeThickness = minPipeThickness;
    }

    public Double getMaxPipeThickness() {
        return maxPipeThickness;
    }

    public void setMaxPipeThickness(Double maxPipeThickness) {
        this.maxPipeThickness = maxPipeThickness;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Double getMinTestPressure() {
        return minTestPressure;
    }

    public void setMinTestPressure(Double minTestPressure) {
        this.minTestPressure = minTestPressure;
    }

    public Double getMaxTestPressure() {
        return maxTestPressure;
    }

    public void setMaxTestPressure(Double maxTestPressure) {
        this.maxTestPressure = maxTestPressure;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getCleaningMedium() {
        return cleaningMedium;
    }

    public void setCleaningMedium(String cleaningMedium) {
        this.cleaningMedium = cleaningMedium;
    }

    public Double getHourNorm() {
        return hourNorm;
    }

    public void setHourNorm(Double hourNorm) {
        this.hourNorm = hourNorm;
    }
}
