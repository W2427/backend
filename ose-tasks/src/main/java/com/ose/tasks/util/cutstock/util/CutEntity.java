package com.ose.tasks.util.cutstock.util;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public class CutEntity implements Comparable<CutEntity> {

    @Schema(description = "实体ID")
    private Long id;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "长度")
    private BigDecimal length;

    @Schema(description = "是否套料")
    private boolean cutted;

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

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public boolean isCutted() {
        return cutted;
    }

    public void setCutted(boolean cutted) {
        this.cutted = cutted;
    }

    @Override
    public int compareTo(CutEntity o) {
        return length.compareTo(o.getLength());
    }
}
