package com.ose.materialspm.dto;

import com.ose.dto.BaseDTO;

/**
 */

public class FadListResultsDTO extends BaseDTO {

    private static final long serialVersionUID = -5725891260099927636L;

    private Object esiStatus;
    private Object iviId;
    private Object mirId;
    private Object lpId;
    private Object ivprId;
    private Object resvQty;
    private Object issueQty;
    private Object issueDate;
    private Object ident;
    private Object whId;
    private Object locId;
    private Object smstId;
    private Object unitId;
    private Object tagNumber;
    private Object heatId;
    private Object plateId;
    private Object identDeviation;
    private Object sasId;
    private Object siteStatInd;
    private Object mirNumber;
    private Object projId;
    private Object heatNumber;
    private Object whCode;
    private Object locCode;
    private Object unitCode;
    private Object smstCode;
    private Object fahId;

    public Object getEsiStatus() {
        return esiStatus;
    }

    public void setEsiStatus(Object esiStatus) {
        this.esiStatus = esiStatus;
    }

    public Object getIviId() {
        return iviId;
    }

    public void setIviId(Object iviId) {
        this.iviId = iviId;
    }

    public Object getMirId() {
        return mirId;
    }

    public void setMirId(Object mirId) {
        this.mirId = mirId;
    }

    public Object getLpId() {
        return lpId;
    }

    public void setLpId(Object lpId) {
        this.lpId = lpId;
    }

    public Object getIvprId() {
        return ivprId;
    }

    public void setIvprId(Object ivprId) {
        this.ivprId = ivprId;
    }

    public Object getResvQty() {
        return resvQty;
    }

    public void setResvQty(Object resvQty) {
        this.resvQty = resvQty;
    }

    public Object getIssueQty() {
        return issueQty;
    }

    public void setIssueQty(Object issueQty) {
        this.issueQty = issueQty;
    }

    public Object getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Object issueDate) {
        this.issueDate = issueDate;
    }

    public Object getIdent() {
        return ident;
    }

    public void setIdent(Object ident) {
        this.ident = ident;
    }

    public Object getWhId() {
        return whId;
    }

    public void setWhId(Object whId) {
        this.whId = whId;
    }

    public Object getLocId() {
        return locId;
    }

    public void setLocId(Object locId) {
        this.locId = locId;
    }

    public Object getSmstId() {
        return smstId;
    }

    public void setSmstId(Object smstId) {
        this.smstId = smstId;
    }

    public Object getUnitId() {
        return unitId;
    }

    public void setUnitId(Object unitId) {
        this.unitId = unitId;
    }

    public Object getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(Object tagNumber) {
        this.tagNumber = tagNumber;
    }

    public Object getHeatId() {
        return heatId;
    }

    public void setHeatId(Object heatId) {
        this.heatId = heatId;
    }

    public Object getPlateId() {
        return plateId;
    }

    public void setPlateId(Object plateId) {
        this.plateId = plateId;
    }

    public Object getIdentDeviation() {
        return identDeviation;
    }

    public void setIdentDeviation(Object identDeviation) {
        this.identDeviation = identDeviation;
    }

    public Object getSasId() {
        return sasId;
    }

    public void setSasId(Object sasId) {
        this.sasId = sasId;
    }

    public Object getSiteStatInd() {
        return siteStatInd;
    }

    public void setSiteStatInd(Object siteStatInd) {
        this.siteStatInd = siteStatInd;
    }

    public Object getMirNumber() {
        return mirNumber;
    }

    public void setMirNumber(Object mirNumber) {
        this.mirNumber = mirNumber;
    }

    public Object getProjId() {
        return projId;
    }

    public void setProjId(Object projId) {
        this.projId = projId;
    }

    public Object getHeatNumber() {
        return heatNumber;
    }

    public void setHeatNumber(Object heatNumber) {
        String heatNo = String.valueOf(heatNumber);
        if(heatNo.indexOf("[")>-1){
            String front = heatNo.substring(0, heatNo.indexOf("[")-1);
            String after = heatNo.substring(heatNo.indexOf("[")+1 , heatNo.indexOf("]"));
            heatNo = front + "/" + after;
            this.heatNumber = heatNo;
        }else{
            this.heatNumber = heatNumber;
        }
    }

    public Object getWhCode() {
        return whCode;
    }

    public void setWhCode(Object whCode) {
        this.whCode = whCode;
    }

    public Object getLocCode() {
        return locCode;
    }

    public void setLocCode(Object locCode) {
        this.locCode = locCode;
    }

    public Object getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(Object unitCode) {
        this.unitCode = unitCode;
    }

    public Object getSmstCode() {
        return smstCode;
    }

    public void setSmstCode(Object smstCode) {
        this.smstCode = smstCode;
    }

    public Object getFahId() {
        return fahId;
    }

    public void setFahId(Object fahId) {
        this.fahId = fahId;
    }
}
