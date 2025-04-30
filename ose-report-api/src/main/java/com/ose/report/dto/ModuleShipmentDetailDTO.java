package com.ose.report.dto;

import com.ose.dto.BaseDTO;
import com.ose.report.dto.moduleProcess.ModuleProcessModuleAssembledDetailItemDTO;

import java.util.List;

public class ModuleShipmentDetailDTO extends BaseDTO {

    private static final long serialVersionUID = -5572396135838284061L;

    private String cuttingPercentage;
    private String fitupPercentage;
    private String weldPercentage;
    private String paintedPercentage;
    private String assembledPercentage;

    private String outFittingPercentage;
    private Double outFittingTotal = 0.0;
    private String cablePercentage;
    private Double cableTotal = 0.0;
    private Double cableFinish = 0.0;
    private String equipmentPercentage;
    private Double equipmentTotal = 0.0;
    private Double equipmentFinish = 0.0;

    private Double weightTotal;
    private Double lengthTotal;
    private Integer qtyTotal;
    private List<ModuleShipmentDetailItemDTO> moduleShipmentDetailItemDTO;

    private List<ModuleProcessModuleAssembledDetailItemDTO> moduleProcessModuleAssembledDetailItemDTOS;

    public List<ModuleShipmentDetailItemDTO> getModuleShipmentDetailItemDTO() {
        return moduleShipmentDetailItemDTO;
    }

    public void setModuleShipmentDetailItemDTO(List<ModuleShipmentDetailItemDTO> moduleShipmentDetailItemDTO) {
        this.moduleShipmentDetailItemDTO = moduleShipmentDetailItemDTO;
    }

    public String getCuttingPercentage() {
        return cuttingPercentage;
    }

    public void setCuttingPercentage(String cuttingPercentage) {
        this.cuttingPercentage = cuttingPercentage;
    }

    public String getFitupPercentage() {
        return fitupPercentage;
    }

    public void setFitupPercentage(String fitupPercentage) {
        this.fitupPercentage = fitupPercentage;
    }

    public String getWeldPercentage() {
        return weldPercentage;
    }

    public void setWeldPercentage(String weldPercentage) {
        this.weldPercentage = weldPercentage;
    }

    public String getPaintedPercentage() {
        return paintedPercentage;
    }

    public void setPaintedPercentage(String paintedPercentage) {
        this.paintedPercentage = paintedPercentage;
    }

    public String getAssembledPercentage() {
        return assembledPercentage;
    }

    public void setAssembledPercentage(String assembledPercentage) {
        this.assembledPercentage = assembledPercentage;
    }

    public Double getWeightTotal() {
        return weightTotal;
    }

    public void setWeightTotal(Double weightTotal) {
        this.weightTotal = weightTotal;
    }

    public List<ModuleProcessModuleAssembledDetailItemDTO> getModuleProcessModuleAssembledDetailItemDTOS() {
        return moduleProcessModuleAssembledDetailItemDTOS;
    }

    public void setModuleProcessModuleAssembledDetailItemDTOS(List<ModuleProcessModuleAssembledDetailItemDTO> moduleProcessModuleAssembledDetailItemDTOS) {
        this.moduleProcessModuleAssembledDetailItemDTOS = moduleProcessModuleAssembledDetailItemDTOS;
    }

    public Double getLengthTotal() {
        return lengthTotal;
    }

    public void setLengthTotal(Double lengthTotal) {
        this.lengthTotal = lengthTotal;
    }

    public Integer getQtyTotal() {
        return qtyTotal;
    }

    public void setQtyTotal(Integer qtyTotal) {
        this.qtyTotal = qtyTotal;
    }

    public String getOutFittingPercentage() {
        return outFittingPercentage;
    }

    public void setOutFittingPercentage(String outFittingPercentage) {
        this.outFittingPercentage = outFittingPercentage;
    }

    public Double getOutFittingTotal() {
        return outFittingTotal;
    }

    public void setOutFittingTotal(Double outFittingTotal) {
        this.outFittingTotal = outFittingTotal;
    }

    public String getCablePercentage() {
        return cablePercentage;
    }

    public void setCablePercentage(String cablePercentage) {
        this.cablePercentage = cablePercentage;
    }

    public Double getCableTotal() {
        return cableTotal;
    }

    public void setCableTotal(Double cableTotal) {
        this.cableTotal = cableTotal;
    }

    public String getEquipmentPercentage() {
        return equipmentPercentage;
    }

    public void setEquipmentPercentage(String equipmentPercentage) {
        this.equipmentPercentage = equipmentPercentage;
    }

    public Double getEquipmentTotal() {
        return equipmentTotal;
    }

    public void setEquipmentTotal(Double equipmentTotal) {
        this.equipmentTotal = equipmentTotal;
    }

    public Double getCableFinish() {
        return cableFinish;
    }

    public void setCableFinish(Double cableFinish) {
        this.cableFinish = cableFinish;
    }

    public Double getEquipmentFinish() {
        return equipmentFinish;
    }

    public void setEquipmentFinish(Double equipmentFinish) {
        this.equipmentFinish = equipmentFinish;
    }
}
