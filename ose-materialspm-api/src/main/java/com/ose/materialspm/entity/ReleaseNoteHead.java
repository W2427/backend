package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

/**
 */

public class ReleaseNoteHead extends BaseDTO {

    private static final long serialVersionUID = -5725891260099927636L;

    private Object poNumber;

    private Object pohId;

    private Object poSupp;

    private Object relnId;

    private Object relnNumber;

    public Object getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(Object poNumber) {
        this.poNumber = poNumber;
    }

    public Object getPoSupp() {
        return poSupp;
    }

    public void setPoSupp(Object poSupp) {
        this.poSupp = poSupp;
    }

    public Object getPohId() {
        return pohId;
    }

    public void setPohId(Object pohId) {
        this.pohId = pohId;
    }

    public Object getRelnId() {
        return relnId;
    }

    public void setRelnId(Object relnId) {
        this.relnId = relnId;
    }

    public Object getRelnNumber() {
        return relnNumber;
    }

    public void setRelnNumber(Object relnNumber) {
        this.relnNumber = relnNumber;
    }

}
