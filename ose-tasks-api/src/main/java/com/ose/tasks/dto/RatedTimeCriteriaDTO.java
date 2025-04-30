package com.ose.tasks.dto;

import com.ose.dto.PageDTO;
import com.ose.tasks.vo.RatedTimeUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class RatedTimeCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -3925042979290466932L;

    @Schema(description = "工序阶段ID")
    private Long processStageId;

    @Schema(description = "工序ID")
    private Long processId;

    @Schema(description = "实体类型ID")
    private Long entitySubTypeId;

    @Schema(description = "单位")
    @Enumerated(EnumType.STRING)
    private RatedTimeUnit unit;

    @Schema(description = "管径")
    private String pipeDiameter;

    @Schema(description = "管子最小厚度")
    private String minPipeThickness;

    @Schema(description = "管子最大厚度")
    private String maxPipeThickness;

    @Schema(description = "材质")
    private String material;

    @Schema(description = "最小试压")
    private String minTestPressure;

    @Schema(description = "最大试压")
    private String maxTestPressure;

    @Schema(description = "介质")
    private String medium;

    @Schema(description = "清洁介质")
    private String cleaningMedium;

    @Schema(description = "人工时")
    private String hourNorm;

    public Long getProcessStageId() {
        return processStageId;
    }

    public void setProcessStageId(Long processStageId) {
        this.processStageId = processStageId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public RatedTimeUnit getUnit() {
        return unit;
    }

    public void setUnit(RatedTimeUnit unit) {
        this.unit = unit;
    }

    public String getPipeDiameter() {
        return pipeDiameter;
    }

    public void setPipeDiameter(String pipeDiameter) {
        this.pipeDiameter = pipeDiameter;
    }

    public String getMinPipeThickness() {
        return minPipeThickness;
    }

    public void setMinPipeThickness(String minPipeThickness) {
        this.minPipeThickness = minPipeThickness;
    }

    public String getMaxPipeThickness() {
        return maxPipeThickness;
    }

    public void setMaxPipeThickness(String maxPipeThickness) {
        this.maxPipeThickness = maxPipeThickness;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMinTestPressure() {
        return minTestPressure;
    }

    public void setMinTestPressure(String minTestPressure) {
        this.minTestPressure = minTestPressure;
    }

    public String getMaxTestPressure() {
        return maxTestPressure;
    }

    public void setMaxTestPressure(String maxTestPressure) {
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

    public String getHourNorm() {
        return hourNorm;
    }

    public void setHourNorm(String hourNorm) {
        this.hourNorm = hourNorm;
    }
}
