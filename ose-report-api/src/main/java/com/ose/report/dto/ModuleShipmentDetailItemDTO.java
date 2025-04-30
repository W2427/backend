package com.ose.report.dto;

import com.ose.dto.BaseDTO;

public class ModuleShipmentDetailItemDTO extends BaseDTO {

    private static final long serialVersionUID = -1475667878525767537L;

    private String moduleNo;

    private String cuttingPercentage;

    private String fitupPercentage;
    private String weldPercentage;
    private String paintedPercentage;
    private String assembledPercentage;

    private String outFittingPercentage;
    private String cablePercentage;
    private String equipmentPercentage;

    private Double weight;

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getOutFittingPercentage() {
        return outFittingPercentage;
    }

    public void setOutFittingPercentage(String outFittingPercentage) {
        this.outFittingPercentage = outFittingPercentage;
    }

    public String getCablePercentage() {
        return cablePercentage;
    }

    public void setCablePercentage(String cablePercentage) {
        this.cablePercentage = cablePercentage;
    }

    public String getEquipmentPercentage() {
        return equipmentPercentage;
    }

    public void setEquipmentPercentage(String equipmentPercentage) {
        this.equipmentPercentage = equipmentPercentage;
    }
}
