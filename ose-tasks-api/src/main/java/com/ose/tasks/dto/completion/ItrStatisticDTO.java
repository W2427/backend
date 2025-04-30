package com.ose.tasks.dto.completion;

import com.ose.dto.BaseDTO;

import java.math.BigDecimal;
import java.math.BigInteger;


public class ItrStatisticDTO extends BaseDTO {

    private static final long serialVersionUID = 1112236077389658131L;
    private String discipline;

    private BigInteger subTotal;

    private BigDecimal hullCnt;

    private BigDecimal topsideCnt;

    private BigDecimal initCnt;

    private BigDecimal initPer;

    private BigDecimal printedCnt;

    private BigDecimal printedPer;

    private BigDecimal signedCnt;

    private BigDecimal signedPer;

    private BigDecimal closedCnt;

    private BigDecimal closedPer;

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public BigInteger getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigInteger subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getHullCnt() {
        return hullCnt;
    }

    public void setHullCnt(BigDecimal hullCnt) {
        this.hullCnt = hullCnt;
    }

    public BigDecimal getTopsideCnt() {
        return topsideCnt;
    }

    public void setTopsideCnt(BigDecimal topsideCnt) {
        this.topsideCnt = topsideCnt;
    }

    public BigDecimal getInitCnt() {
        return initCnt;
    }

    public void setInitCnt(BigDecimal initCnt) {
        this.initCnt = initCnt;
    }

    public BigDecimal getInitPer() {
        return initPer;
    }

    public void setInitPer(BigDecimal initPer) {
        this.initPer = initPer;
    }

    public BigDecimal getPrintedCnt() {
        return printedCnt;
    }

    public void setPrintedCnt(BigDecimal printedCnt) {
        this.printedCnt = printedCnt;
    }

    public BigDecimal getPrintedPer() {
        return printedPer;
    }

    public void setPrintedPer(BigDecimal printedPer) {
        this.printedPer = printedPer;
    }

    public BigDecimal getSignedCnt() {
        return signedCnt;
    }

    public void setSignedCnt(BigDecimal signedCnt) {
        this.signedCnt = signedCnt;
    }

    public BigDecimal getSignedPer() {
        return signedPer;
    }

    public void setSignedPer(BigDecimal signedPer) {
        this.signedPer = signedPer;
    }

    public BigDecimal getClosedCnt() {
        return closedCnt;
    }

    public void setClosedCnt(BigDecimal closedCnt) {
        this.closedCnt = closedCnt;
    }

    public BigDecimal getClosedPer() {
        return closedPer;
    }

    public void setClosedPer(BigDecimal closedPer) {
        this.closedPer = closedPer;
    }
}
