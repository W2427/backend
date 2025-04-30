package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;


public class CuttingExportItemDTO extends BaseDTO {

    private static final long serialVersionUID = -6462963980112938360L;

    @Schema(description = "ISO号")
    private String drawingNo;

    @Schema(description = "版本")
    private String revision;

    @Schema(description = "管段号")
    private String pipeNo;

    @Schema(description = "材质")
    private String material;

    @Schema(description = "尺寸")
    private String size;

    @Schema(description = "数量")
    private BigDecimal qty;

    @Schema(description = "炉批号")
    private String heatNo;

    @Schema(description = "切割时间")
    private String cuttingTime;

    @Schema(description = "Bevelling时间")
    private String bevellingTime;

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getPipeNo() {
        return pipeNo;
    }

    public void setPipeNo(String pipeNo) {
        this.pipeNo = pipeNo;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getCuttingTime() {
        return cuttingTime;
    }

    public void setCuttingTime(String cuttingTime) {
        this.cuttingTime = cuttingTime;
    }

    public String getBevellingTime() {
        return bevellingTime;
    }

    public void setBevellingTime(String bevellingTime) {
        this.bevellingTime = bevellingTime;
    }
}
