package com.ose.tasks.util.cutstock.util;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public class MaterialEntity implements Comparable<MaterialEntity> {

    @Schema(description = "材料ID")
    private Long id;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "0 库存材料；1 现场余料")
    private short materialType;

    @Schema(description = "长度")
    private BigDecimal length;

    @Schema(description = "余料长度，反写预留，暂不用")
    private BigDecimal scrapLength;

    @Schema(description = "是否使用")
    private boolean used = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public short getMaterialType() {
        return materialType;
    }

    public void setMaterialType(short materialType) {
        this.materialType = materialType;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getScrapLength() {
        return scrapLength;
    }

    public void setScrapLength(BigDecimal scrapLength) {
        this.scrapLength = scrapLength;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public int compareTo(MaterialEntity o) {
        return length.compareTo(o.getLength());
    }
}
