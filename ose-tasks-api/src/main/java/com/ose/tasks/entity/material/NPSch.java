package com.ose.tasks.entity.material;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "npsch")
public class NPSch extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long npsId;

    //壁厚等级
    private String thicknessSch;

    //壁厚
    private double thickness;

    //完整规格
    private String pipeSpec;

    //单位重量
    private String unitWeightKg;

    //外表面单位面积
    private String outerUnitAreaM2;

    //内表面单位面积
    private String innerUnitAreaM2;

    //单位截面积
    private String sectionAreaM2;

    //公称直径
    private String nps;

    //外径值
    private String outerDiameterMm;

    //寸径值
    private String npsIn;

    //规范依据
    private String criterionCode;

    public Long getNpsId() {
        return npsId;
    }

    public void setNpsId(Long npsId) {
        this.npsId = npsId;
    }

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
