package com.ose.report.dto.moduleProcess;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class ModuleProcessModuleAssembledStaticItemDTO extends BaseDTO {

    private static final long serialVersionUID = 642441808200956956L;

    @Schema(description = "模块批次号/区域号")
    private String shipmentNo;

    @Schema(description = "模块号")
    private String moduleNo;

    @Schema(description = "专业")
    private String discipline;

    @Schema(description = "栏杆-Handrail完成量")
    private Double handrailFinishQty = 0.0;

    @Schema(description = "栏杆-Handrail设计量")
    private Double handrailTotalQty = 0.0;

    @Schema(description = "栏杆-Handrail单位")
    private String handrailUnit;

    @Schema(description = "格栅-Grating完成量")
    private Double gratingFinishQty = 0.0;

    @Schema(description = "格栅-Grating设计量")
    private Double gratingTotalQty = 0.0;

    @Schema(description = "格栅-Grating单位")
    private String gratingUnit;

    @Schema(description = "花钢板-Floor Plate完成量")
    private Double floorPlateFinishQty = 0.0;

    @Schema(description = "花钢板-Floor Plate设计量")
    private Double floorPlateTotalQty = 0.0;

    @Schema(description = "花钢板-Floor Plate单位")
    private String floorPlateUnit;

    @Schema(description = "踏步-Tread完成量")
    private Double threadFinishQty = 0.0;

    @Schema(description = "踏步-Tread设计量")
    private Double threadTotalQty = 0.0;

    @Schema(description = "踏步-Tread单位")
    private String threadUnit;

    @Schema(description = "管系管段完成长度")
    private Double pipeLengthFinish = 0.0;

    @Schema(description = "管系管段总长度长度")
    private Double pipeLengthTotal = 0.0;

    @Schema(description = "管系管段长度单位")
    private String pipeLengthUnit;

    @Schema(description = "管系管附件完成数量")
    private Double componentQtyFinish = 0.0;

    @Schema(description = "管系管附件总数量")
    private Double componentQtyTotal = 0.0;

    @Schema(description = "管系管附件数量单位")
    private String componentQtyUnit;

    @Schema(description = "电气电缆完成数量")
    private Double cableLadderQtyFinish = 0.0;

    @Schema(description = "电气电缆总数量")
    private Double cableLadderQtyTotal = 0.0;

    @Schema(description = "电气电缆单位")
    private String cableLadderQtyUnit;

    @Schema(description = "电气电缆完成数量")
    private Double cableLadderOtherQtyFinish = 0.0;

    @Schema(description = "电气电缆总数量")
    private Double cableLadderOtherQtyTotal = 0.0;

    @Schema(description = "电气电缆单位")
    private String cableLadderOtherQtyUnit;

    @Schema(description = "电气管道完成数量")
    private Double conduitQtyFinish = 0.0;

    @Schema(description = "电气管道总数量")
    private Double conduitQtyTotal = 0.0;

    @Schema(description = "电气管道单位")
    private String conduitQtyUnit;

    @Schema(description = "电气焊件完成数量")
    private Double unistrutQtyFinish = 0.0;

    @Schema(description = "电气焊件总数量")
    private Double unistrutQtyTotal = 0.0;

    @Schema(description = "电气焊件单位")
    private String unistrutQtyUnit;

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public Double getHandrailFinishQty() {
        return handrailFinishQty;
    }

    public void setHandrailFinishQty(Double handrailFinishQty) {
        this.handrailFinishQty = handrailFinishQty;
    }

    public Double getHandrailTotalQty() {
        return handrailTotalQty;
    }

    public void setHandrailTotalQty(Double handrailTotalQty) {
        this.handrailTotalQty = handrailTotalQty;
    }

    public String getHandrailUnit() {
        return handrailUnit;
    }

    public void setHandrailUnit(String handrailUnit) {
        this.handrailUnit = handrailUnit;
    }

    public Double getGratingFinishQty() {
        return gratingFinishQty;
    }

    public void setGratingFinishQty(Double gratingFinishQty) {
        this.gratingFinishQty = gratingFinishQty;
    }

    public Double getGratingTotalQty() {
        return gratingTotalQty;
    }

    public void setGratingTotalQty(Double gratingTotalQty) {
        this.gratingTotalQty = gratingTotalQty;
    }

    public String getGratingUnit() {
        return gratingUnit;
    }

    public void setGratingUnit(String gratingUnit) {
        this.gratingUnit = gratingUnit;
    }

    public Double getFloorPlateFinishQty() {
        return floorPlateFinishQty;
    }

    public void setFloorPlateFinishQty(Double floorPlateFinishQty) {
        this.floorPlateFinishQty = floorPlateFinishQty;
    }

    public Double getFloorPlateTotalQty() {
        return floorPlateTotalQty;
    }

    public void setFloorPlateTotalQty(Double floorPlateTotalQty) {
        this.floorPlateTotalQty = floorPlateTotalQty;
    }

    public String getFloorPlateUnit() {
        return floorPlateUnit;
    }

    public void setFloorPlateUnit(String floorPlateUnit) {
        this.floorPlateUnit = floorPlateUnit;
    }

    public Double getThreadFinishQty() {
        return threadFinishQty;
    }

    public void setThreadFinishQty(Double threadFinishQty) {
        this.threadFinishQty = threadFinishQty;
    }

    public Double getThreadTotalQty() {
        return threadTotalQty;
    }

    public void setThreadTotalQty(Double threadTotalQty) {
        this.threadTotalQty = threadTotalQty;
    }

    public String getThreadUnit() {
        return threadUnit;
    }

    public void setThreadUnit(String threadUnit) {
        this.threadUnit = threadUnit;
    }

    public Double getPipeLengthFinish() {
        return pipeLengthFinish;
    }

    public void setPipeLengthFinish(Double pipeLengthFinish) {
        this.pipeLengthFinish = pipeLengthFinish;
    }

    public Double getPipeLengthTotal() {
        return pipeLengthTotal;
    }

    public void setPipeLengthTotal(Double pipeLengthTotal) {
        this.pipeLengthTotal = pipeLengthTotal;
    }

    public String getPipeLengthUnit() {
        return pipeLengthUnit;
    }

    public void setPipeLengthUnit(String pipeLengthUnit) {
        this.pipeLengthUnit = pipeLengthUnit;
    }

    public Double getComponentQtyFinish() {
        return componentQtyFinish;
    }

    public void setComponentQtyFinish(Double componentQtyFinish) {
        this.componentQtyFinish = componentQtyFinish;
    }

    public Double getComponentQtyTotal() {
        return componentQtyTotal;
    }

    public void setComponentQtyTotal(Double componentQtyTotal) {
        this.componentQtyTotal = componentQtyTotal;
    }

    public String getComponentQtyUnit() {
        return componentQtyUnit;
    }

    public void setComponentQtyUnit(String componentQtyUnit) {
        this.componentQtyUnit = componentQtyUnit;
    }

    public Double getCableLadderQtyFinish() {
        return cableLadderQtyFinish;
    }

    public void setCableLadderQtyFinish(Double cableLadderQtyFinish) {
        this.cableLadderQtyFinish = cableLadderQtyFinish;
    }

    public Double getCableLadderQtyTotal() {
        return cableLadderQtyTotal;
    }

    public void setCableLadderQtyTotal(Double cableLadderQtyTotal) {
        this.cableLadderQtyTotal = cableLadderQtyTotal;
    }

    public String getCableLadderQtyUnit() {
        return cableLadderQtyUnit;
    }

    public void setCableLadderQtyUnit(String cableLadderQtyUnit) {
        this.cableLadderQtyUnit = cableLadderQtyUnit;
    }

    public Double getConduitQtyFinish() {
        return conduitQtyFinish;
    }

    public void setConduitQtyFinish(Double conduitQtyFinish) {
        this.conduitQtyFinish = conduitQtyFinish;
    }

    public Double getConduitQtyTotal() {
        return conduitQtyTotal;
    }

    public void setConduitQtyTotal(Double conduitQtyTotal) {
        this.conduitQtyTotal = conduitQtyTotal;
    }

    public String getConduitQtyUnit() {
        return conduitQtyUnit;
    }

    public void setConduitQtyUnit(String conduitQtyUnit) {
        this.conduitQtyUnit = conduitQtyUnit;
    }

    public Double getUnistrutQtyFinish() {
        return unistrutQtyFinish;
    }

    public void setUnistrutQtyFinish(Double unistrutQtyFinish) {
        this.unistrutQtyFinish = unistrutQtyFinish;
    }

    public Double getUnistrutQtyTotal() {
        return unistrutQtyTotal;
    }

    public void setUnistrutQtyTotal(Double unistrutQtyTotal) {
        this.unistrutQtyTotal = unistrutQtyTotal;
    }

    public String getUnistrutQtyUnit() {
        return unistrutQtyUnit;
    }

    public void setUnistrutQtyUnit(String unistrutQtyUnit) {
        this.unistrutQtyUnit = unistrutQtyUnit;
    }

    public Double getCableLadderOtherQtyFinish() {
        return cableLadderOtherQtyFinish;
    }

    public void setCableLadderOtherQtyFinish(Double cableLadderOtherQtyFinish) {
        this.cableLadderOtherQtyFinish = cableLadderOtherQtyFinish;
    }

    public Double getCableLadderOtherQtyTotal() {
        return cableLadderOtherQtyTotal;
    }

    public void setCableLadderOtherQtyTotal(Double cableLadderOtherQtyTotal) {
        this.cableLadderOtherQtyTotal = cableLadderOtherQtyTotal;
    }

    public String getCableLadderOtherQtyUnit() {
        return cableLadderOtherQtyUnit;
    }

    public void setCableLadderOtherQtyUnit(String cableLadderOtherQtyUnit) {
        this.cableLadderOtherQtyUnit = cableLadderOtherQtyUnit;
    }
}
