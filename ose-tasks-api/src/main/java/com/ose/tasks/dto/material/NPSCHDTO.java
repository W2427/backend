package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 数据传输对象。
 */
public class NPSCHDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;


    @Schema(description = "壁厚等级")
    private String thicknessSch;

    @Schema(description = "壁厚")
    private double thickness;

    @Schema(description = "完整规格")
    private String pipeSpec;

    @Schema(description = "单位重量")
    private String unitWeightKg;

    @Schema(description = "外表面单位面积")
    private String outerUnitAreaM2;

    @Schema(description = "内表面单位面积")
    private String innerUnitAreaM2;

    @Schema(description = "单位截面积")
    private String sectionAreaM2;

    @Schema(description = "公称直径")
    private String nps;

    @Schema(description = "外径值")
    private String outerDiameterMm;

    @Schema(description = "寸径值")
    private String npsIn;

    @Schema(description = "规范依据")
    private String criterionCode;

    public String getThicknessSch() {
        return thicknessSch;
    }

    public void setThicknessSch(String thicknessSch) {
        this.thicknessSch = thicknessSch;
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    public String getPipeSpec() {
        return pipeSpec;
    }

    public void setPipeSpec(String pipeSpec) {
        this.pipeSpec = pipeSpec;
    }

    public String getUnitWeightKg() {
        return unitWeightKg;
    }

    public void setUnitWeightKg(String unitWeightKg) {
        this.unitWeightKg = unitWeightKg;
    }

    public String getOuterUnitAreaM2() {
        return outerUnitAreaM2;
    }

    public void setOuterUnitAreaM2(String outerUnitAreaM2) {
        this.outerUnitAreaM2 = outerUnitAreaM2;
    }

    public String getInnerUnitAreaM2() {
        return innerUnitAreaM2;
    }

    public void setInnerUnitAreaM2(String innerUnitAreaM2) {
        this.innerUnitAreaM2 = innerUnitAreaM2;
    }

    public String getSectionAreaM2() {
        return sectionAreaM2;
    }

    public void setSectionAreaM2(String sectionAreaM2) {
        this.sectionAreaM2 = sectionAreaM2;
    }

    public String getNps() {
        return nps;
    }

    public void setNps(String nps) {
        this.nps = nps;
    }

    public String getOuterDiameterMm() {
        return outerDiameterMm;
    }

    public void setOuterDiameterMm(String outerDiameterMm) {
        this.outerDiameterMm = outerDiameterMm;
    }

    public String getNpsIn() {
        return npsIn;
    }

    public void setNpsIn(String npsIn) {
        this.npsIn = npsIn;
    }

    public String getCriterionCode() {
        return criterionCode;
    }

    public void setCriterionCode(String criterionCode) {
        this.criterionCode = criterionCode;
    }

}
