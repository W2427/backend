package com.ose.tasks.entity.material;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "nps")
public class NPS extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    //公称直径
    private String nps;

    //外径值
    private String outerDiameterMm;

    //nps值
    private String npsValue;

    //规范依据
    private String criterionCode;

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

    public String getNpsValue() {
        return npsValue;
    }

    public void setNpsValue(String npsValue) {
        this.npsValue = npsValue;
    }

    public String getCriterionCode() {
        return criterionCode;
    }

    public void setCriterionCode(String criterionCode) {
        this.criterionCode = criterionCode;
    }

}
