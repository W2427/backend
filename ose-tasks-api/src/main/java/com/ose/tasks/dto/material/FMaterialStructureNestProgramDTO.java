package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class FMaterialStructureNestProgramDTO extends BaseDTO {

    private static final long serialVersionUID = -3690578331039714427L;

    @Schema(description = "厚度")
    private String thickness;

    @Schema(description = "宽度")
    private String width;

    @Schema(description = "长度")
    private String length;

    @Schema(description = "材料")
    private String material;

    @Schema(description = "利用率")
    private String utilizationRatio;

    @Schema(description = "产生余料名")
    private String remainderCreated;

    @Schema(description = "使用余料名")
    private String remainderUsed;

    @Schema(description = "材料二维码")
    private String materialQrCode;

    @Schema(description = "材料炉批号")
    private String materialHeatNo;

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getUtilizationRatio() {
        return utilizationRatio;
    }

    public void setUtilizationRatio(String utilizationRatio) {
        this.utilizationRatio = utilizationRatio;
    }

    public String getMaterialQrCode() {
        return materialQrCode;
    }

    public void setMaterialQrCode(String materialQrCode) {
        this.materialQrCode = materialQrCode;
    }

    public String getMaterialHeatNo() {
        return materialHeatNo;
    }

    public void setMaterialHeatNo(String materialHeatNo) {
        this.materialHeatNo = materialHeatNo;
    }

    public String getRemainderCreated() {
        return remainderCreated;
    }

    public void setRemainderCreated(String remainderCreated) {
        this.remainderCreated = remainderCreated;
    }

    public String getRemainderUsed() {
        return remainderUsed;
    }

    public void setRemainderUsed(String remainderUsed) {
        this.remainderUsed = remainderUsed;
    }
}
