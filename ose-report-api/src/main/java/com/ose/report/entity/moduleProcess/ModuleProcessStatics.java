package com.ose.report.entity.moduleProcess;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 模块完成进度统计归档数据实体。
 */
@Entity
@Table(name = "module_process_statistics")
public class ModuleProcessStatics extends BaseBizEntity {

    private static final long serialVersionUID = -7277423184520885913L;

    @Schema(name = "项目 ID")
    @Column
    private Long projectId;

    @Schema(name = "组织 ID")
    @Column
    private Long orgId;

    @Schema(name = "专业")
    @Column
    private String discipline;

    @Schema(name = "归档时间（年）")
    @Column
    private Integer archiveYear;

    @Schema(name = "归档时间（月）")
    @Column
    private Integer archiveMonth;

    @Schema(name = "归档时间（日）")
    @Column
    private Integer archiveDay;

    @Schema(name = "归档时间（周）")
    @Column
    private Integer archiveWeek;

    @Schema(name = "归档时间")
    @Column
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date archiveDate;

    @Schema(description = "模块批次号/区域号")
    @Column
    private String shipmentNo;

    @Schema(description = "模块号")
    @Column
    private String moduleNo;

    @Schema(description = "下料完成数")
    @Column
    private Integer cuttingFinishQty = 0;

    @Schema(description = "下料总数")
    @Column
    private Integer cuttingTotalQty = 0;

    @Schema(description = "预制完成数")
    private Integer fitUpFinishQty = 0;

    @Schema(description = "预制总数")
    private Integer fitUpTotalQty = 0;

    @Schema(description = "焊接完成数")
    private Integer weldFinishQty = 0;

    @Schema(description = "焊接总数")
    private Integer weldTotalQty = 0;

    @Schema(description = "涂装完成数")
    private Integer paintedFinishQty = 0;

    @Schema(description = "涂装总数")
    private Integer paintedTotalQty = 0;

    @Schema(description = "模块安装完成数")
    private Double moduleAssembledFinishQty = 0.0;

    @Schema(description = "模块安装总数")
    private Double moduleAssembledTotalQty = 0.0;

    @Schema(description = "寸径总数")
    private Double lengthTotal = 0.0;

    @Schema(description = "重量总数")
    private Double weightTotal = 0.0;

    @Schema(description = "总数")
    private Integer qtyTotal = 0;

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

    @Schema(description = "电气outfitting检验量")
    private Double outFittingFinishQty = 0.0;

    @Schema(description = "电气outfitting完成量")
    private Double outFittingTotalQty = 0.0;

    @Schema(description = "电气cable检验量")
    private Double cableFinishQty = 0.0;

    @Schema(description = "电气cable完成量")
    private Double cableTotalQty = 0.0;

    @Schema(description = "电气equipment检验量")
    private Double equipmentFinishQty = 0.0;

    @Schema(description = "电气equipment完成量")
    private Double equipmentTotalQty = 0.0;

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

    @Schema(description = "功能块")
    private String funcPart;

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public Integer getArchiveYear() {
        return archiveYear;
    }

    public void setArchiveYear(Integer archiveYear) {
        this.archiveYear = archiveYear;
    }

    public Integer getArchiveMonth() {
        return archiveMonth;
    }

    public void setArchiveMonth(Integer archiveMonth) {
        this.archiveMonth = archiveMonth;
    }

    public Integer getArchiveDay() {
        return archiveDay;
    }

    public void setArchiveDay(Integer archiveDay) {
        this.archiveDay = archiveDay;
    }

    public Integer getArchiveWeek() {
        return archiveWeek;
    }

    public void setArchiveWeek(Integer archiveWeek) {
        this.archiveWeek = archiveWeek;
    }

    public Date getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(Date archiveDate) {
        this.archiveDate = archiveDate;
    }

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public Integer getCuttingFinishQty() {
        return cuttingFinishQty;
    }

    public void setCuttingFinishQty(Integer cuttingFinishQty) {
        this.cuttingFinishQty = cuttingFinishQty;
    }

    public Integer getCuttingTotalQty() {
        return cuttingTotalQty;
    }

    public void setCuttingTotalQty(Integer cuttingTotalQty) {
        this.cuttingTotalQty = cuttingTotalQty;
    }

    public Integer getFitUpFinishQty() {
        return fitUpFinishQty;
    }

    public void setFitUpFinishQty(Integer fitUpFinishQty) {
        this.fitUpFinishQty = fitUpFinishQty;
    }

    public Integer getFitUpTotalQty() {
        return fitUpTotalQty;
    }

    public void setFitUpTotalQty(Integer fitUpTotalQty) {
        this.fitUpTotalQty = fitUpTotalQty;
    }

    public Integer getWeldFinishQty() {
        return weldFinishQty;
    }

    public void setWeldFinishQty(Integer weldFinishQty) {
        this.weldFinishQty = weldFinishQty;
    }

    public Integer getWeldTotalQty() {
        return weldTotalQty;
    }

    public void setWeldTotalQty(Integer weldTotalQty) {
        this.weldTotalQty = weldTotalQty;
    }

    public Integer getPaintedFinishQty() {
        return paintedFinishQty;
    }

    public void setPaintedFinishQty(Integer paintedFinishQty) {
        this.paintedFinishQty = paintedFinishQty;
    }

    public Integer getPaintedTotalQty() {
        return paintedTotalQty;
    }

    public void setPaintedTotalQty(Integer paintedTotalQty) {
        this.paintedTotalQty = paintedTotalQty;
    }

    public Double getModuleAssembledFinishQty() {
        return moduleAssembledFinishQty;
    }

    public void setModuleAssembledFinishQty(Double moduleAssembledFinishQty) {
        this.moduleAssembledFinishQty = moduleAssembledFinishQty;
    }

    public Double getModuleAssembledTotalQty() {
        return moduleAssembledTotalQty;
    }

    public void setModuleAssembledTotalQty(Double moduleAssembledTotalQty) {
        this.moduleAssembledTotalQty = moduleAssembledTotalQty;
    }

    public Double getWeightTotal() {
        return weightTotal;
    }

    public void setWeightTotal(Double weightTotal) {
        this.weightTotal = weightTotal;
    }

    public Double getLengthTotal() {
        return lengthTotal;
    }

    public void setLengthTotal(Double lengthTotal) {
        this.lengthTotal = lengthTotal;
    }

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
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

    public Integer getQtyTotal() {
        return qtyTotal;
    }

    public void setQtyTotal(Integer qtyTotal) {
        this.qtyTotal = qtyTotal;
    }

    public Double getOutFittingFinishQty() {
        return outFittingFinishQty;
    }

    public void setOutFittingFinishQty(Double outFittingFinishQty) {
        this.outFittingFinishQty = outFittingFinishQty;
    }

    public Double getOutFittingTotalQty() {
        return outFittingTotalQty;
    }

    public void setOutFittingTotalQty(Double outFittingTotalQty) {
        this.outFittingTotalQty = outFittingTotalQty;
    }

    public Double getCableFinishQty() {
        return cableFinishQty;
    }

    public void setCableFinishQty(Double cableFinishQty) {
        this.cableFinishQty = cableFinishQty;
    }

    public Double getCableTotalQty() {
        return cableTotalQty;
    }

    public void setCableTotalQty(Double cableTotalQty) {
        this.cableTotalQty = cableTotalQty;
    }

    public Double getEquipmentFinishQty() {
        return equipmentFinishQty;
    }

    public void setEquipmentFinishQty(Double equipmentFinishQty) {
        this.equipmentFinishQty = equipmentFinishQty;
    }

    public Double getEquipmentTotalQty() {
        return equipmentTotalQty;
    }

    public void setEquipmentTotalQty(Double equipmentTotalQty) {
        this.equipmentTotalQty = equipmentTotalQty;
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

    public String getPipeLengthUnit() {
        return pipeLengthUnit;
    }

    public void setPipeLengthUnit(String pipeLengthUnit) {
        this.pipeLengthUnit = pipeLengthUnit;
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
