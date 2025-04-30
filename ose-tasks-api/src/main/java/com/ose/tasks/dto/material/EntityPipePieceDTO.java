package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import com.ose.vo.unit.LengthUnit;

public class EntityPipePieceDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    private String materialCode;

    private String material;

    private String npsText;

    private LengthUnit lengthUnit;

    private Double length;

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getNpsText() {
        return npsText;
    }

    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    public LengthUnit getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(LengthUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

}
