package com.ose.tasks.util.cutstock.util;

import java.math.BigDecimal;
import java.util.Set;

public class CutStockResult {

    private Long materialId;

    private BigDecimal materialLength;

    private Set<CutEntity> nestEntities;

    private BigDecimal surplusLength;

    private BigDecimal scrapLength;

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public BigDecimal getMaterialLength() {
        return materialLength;
    }

    public void setMaterialLength(BigDecimal materialLength) {
        this.materialLength = materialLength;
    }

    public Set<CutEntity> getNestEntities() {
        return nestEntities;
    }

    public void setNestEntities(Set<CutEntity> nestEntities) {
        this.nestEntities = nestEntities;
    }

    public BigDecimal getSurplusLength() {
        return surplusLength;
    }

    public void setSurplusLength(BigDecimal surplusLength) {
        this.surplusLength = surplusLength;
    }

    public BigDecimal getScrapLength() {
        return scrapLength;
    }

    public void setScrapLength(BigDecimal scrapLength) {
        this.scrapLength = scrapLength;
    }
}
