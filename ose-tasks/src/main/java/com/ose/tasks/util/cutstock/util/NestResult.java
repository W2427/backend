package com.ose.tasks.util.cutstock.util;

import java.math.BigDecimal;
import java.util.List;

public class NestResult {

    private boolean nestResult;

    private String executionTime;

    private String nestResultDesc;

    private List<CutStockResult> csResults;

    private BigDecimal surplusSumLength;

    private BigDecimal materialSumLength;

    private BigDecimal cutSumLength;

    private BigDecimal scrapSumLength;

    public boolean isNestResult() {
        return nestResult;
    }

    public void setNestResult(boolean nestResult) {
        this.nestResult = nestResult;
    }

    public List<CutStockResult> getCsResults() {
        return csResults;
    }

    public void setCsResults(List<CutStockResult> csResults) {
        this.csResults = csResults;
    }

    public BigDecimal getSurplusSumLength() {
        return surplusSumLength;
    }

    public void setSurplusSumLength(BigDecimal surplusSumLength) {
        this.surplusSumLength = surplusSumLength;
    }

    public BigDecimal getMaterialSumLength() {
        return materialSumLength;
    }

    public void setMaterialSumLength(BigDecimal materialSumLength) {
        this.materialSumLength = materialSumLength;
    }

    public BigDecimal getCutSumLength() {
        return cutSumLength;
    }

    public void setCutSumLength(BigDecimal cutSumLength) {
        this.cutSumLength = cutSumLength;
    }

    public BigDecimal getScrapSumLength() {
        return scrapSumLength;
    }

    public void setScrapSumLength(BigDecimal scrapSumLength) {
        this.scrapSumLength = scrapSumLength;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public String getNestResultDesc() {
        return nestResultDesc;
    }

    public void setNestResultDesc(String nestResultDesc) {
        this.nestResultDesc = nestResultDesc;
    }
}
