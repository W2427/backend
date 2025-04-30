package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

public class EntityComponentDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    private String materialCode;

    private String material;

    private String npsText;

    private Double qty;

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

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }
}
