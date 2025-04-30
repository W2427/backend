package com.ose.materialspm.dto;

import com.ose.dto.BaseDTO;

/**
 */

public class FaListResultsDTO extends BaseDTO {

    private static final long serialVersionUID = -5725891260099927636L;

    private Object fahId;

    private Object projId;

    private Object fahCode;

    private Object runNumber;

    private Object userId;

    private Object dpId;

    private Object lstId;

    private Object fahType;

    private Object jobStatus;

    public Object getFahId() {
        return fahId;
    }

    public void setFahId(Object fahId) {
        this.fahId = fahId;
    }

    public Object getProjId() {
        return projId;
    }

    public void setProjId(Object projId) {
        this.projId = projId;
    }

    public Object getFahCode() {
        return fahCode;
    }

    public void setFahCode(Object fahCode) {
        this.fahCode = fahCode;
    }

    public Object getRunNumber() {
        return runNumber;
    }

    public void setRunNumber(Object runNumber) {
        this.runNumber = runNumber;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getDpId() {
        return dpId;
    }

    public void setDpId(Object dpId) {
        this.dpId = dpId;
    }

    public Object getLstId() {
        return lstId;
    }

    public void setLstId(Object lstId) {
        this.lstId = lstId;
    }

    public Object getFahType() {
        return fahType;
    }

    public void setFahType(Object fahType) {
        this.fahType = fahType;
    }

    public Object getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Object jobStatus) {
        this.jobStatus = jobStatus;
    }
}
