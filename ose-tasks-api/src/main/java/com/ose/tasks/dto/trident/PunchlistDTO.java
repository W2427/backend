package com.ose.tasks.dto.trident;

import com.ose.dto.BaseDTO;


public class PunchlistDTO extends BaseDTO {

    private static final long serialVersionUID = 5036134919374463891L;
    private Integer plTotal;

    private Integer plClosedTotal;

    private Integer plATotal;

    private Integer plAClosedTotal;

    private Integer plBTotal;

    private Integer plBClosedTotal;

    private Integer plCTotal;

    private Integer plCClosedTotal;

    private String inCharge;

    private String comments;

    public Integer getPlTotal() {
        return plTotal;
    }

    public void setPlTotal(Integer plTotal) {
        this.plTotal = plTotal;
    }

    public Integer getPlClosedTotal() {
        return plClosedTotal;
    }

    public void setPlClosedTotal(Integer plClosedTotal) {
        this.plClosedTotal = plClosedTotal;
    }

    public Integer getPlATotal() {
        return plATotal;
    }

    public void setPlATotal(Integer plATotal) {
        this.plATotal = plATotal;
    }

    public Integer getPlAClosedTotal() {
        return plAClosedTotal;
    }

    public void setPlAClosedTotal(Integer plAClosedTotal) {
        this.plAClosedTotal = plAClosedTotal;
    }

    public Integer getPlBTotal() {
        return plBTotal;
    }

    public void setPlBTotal(Integer plBTotal) {
        this.plBTotal = plBTotal;
    }

    public Integer getPlBClosedTotal() {
        return plBClosedTotal;
    }

    public void setPlBClosedTotal(Integer plBClosedTotal) {
        this.plBClosedTotal = plBClosedTotal;
    }

    public Integer getPlCTotal() {
        return plCTotal;
    }

    public void setPlCTotal(Integer plCTotal) {
        this.plCTotal = plCTotal;
    }

    public Integer getPlCClosedTotal() {
        return plCClosedTotal;
    }

    public void setPlCClosedTotal(Integer plCClosedTotal) {
        this.plCClosedTotal = plCClosedTotal;
    }

    public String getInCharge() {
        return inCharge;
    }

    public void setInCharge(String inCharge) {
        this.inCharge = inCharge;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
