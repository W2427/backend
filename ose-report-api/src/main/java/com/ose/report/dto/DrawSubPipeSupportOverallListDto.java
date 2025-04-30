package com.ose.report.dto;

public class DrawSubPipeSupportOverallListDto extends BaseReportDTO {
    private static final long serialVersionUID = 19506801711784147L;
    private String seq;
    private String materialNO;
    private String specifications;
    private String material;
    private String count;
    private String unit;

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getMaterialNO() {
        return materialNO;
    }

    public void setMaterialNO(String materialNO) {
        this.materialNO = materialNO;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
