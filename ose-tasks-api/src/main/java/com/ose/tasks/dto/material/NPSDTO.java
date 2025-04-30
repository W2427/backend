package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 数据传输对象。
 */
public class NPSDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "公称直径")
    private String nps;

    @Schema(description = "外径值")
    private String outerDiameterMm;

    @Schema(description = "nps值")
    private String npsValue;

    @Schema(description = "规范依据")
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
