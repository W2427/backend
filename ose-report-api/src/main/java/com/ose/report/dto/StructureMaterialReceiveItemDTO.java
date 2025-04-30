package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * 结构材料入库报表信息
 */
public class StructureMaterialReceiveItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 4598964430889873446L;

    @Schema(description = "材料名称")
    private String materialName;

    @Schema(description = "材质")
    private String texture;

    @Schema(description = "规格")
    private String spec;

    @Schema(description = "重量")
    private String weight;

    @Schema(description = "炉号")
    private String heatNoCode;

    @Schema(description = "钢板号")
    private String batchNoCode;

    @Schema(description = "批次号")
    private String batchNumber;

    @Schema(description = "生产厂家")
    private String manufacturer;

    @Schema(description = "证书号")
    private String certCode;

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeatNoCode() {
        return heatNoCode;
    }

    public void setHeatNoCode(String heatNoCode) {
        this.heatNoCode = heatNoCode;
    }

    public String getBatchNoCode() {
        return batchNoCode;
    }

    public void setBatchNoCode(String batchNoCode) {
        this.batchNoCode = batchNoCode;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCertCode() {
        return certCode;
    }

    public void setCertCode(String certCode) {
        this.certCode = certCode;
    }
}
