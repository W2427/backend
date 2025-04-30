package com.ose.tasks.dto.wps;

import com.ose.dto.PageDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class WelderCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -462363128749030855L;

    @Schema(description = "外包商ID")
    private Long subConId;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "焊工类型")
    private String type;

    @Schema(description = "母材")
    private String baseMetal;

    @Schema(description = "NPS壁厚")
    private String nps;

    @Schema(description = "NPS壁厚等级")
    private String sch;

    @Schema(description = "最小厚度")
    private String minThickness;

    @Schema(description = "是否包含最小厚度")
    private String containMinThickness;

    @Schema(description = "最大厚度")
    private String maxThickness;

    @Schema(description = "是否包含最大厚度")
    private String containMaxThickness;

    @Schema(description = "最小直径")
    private String minDia;

    @Schema(description = "是否包含最小直径")
    private String containMinDia;

    @Schema(description = "最大直径")
    private String maxDia;

    @Schema(description = "是否包含最大直径")
    private String containMaxDia;

    public Long getSubConId() {
        return subConId;
    }

    public void setSubConId(Long subConId) {
        this.subConId = subConId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBaseMetal() {
        return baseMetal;
    }

    public void setBaseMetal(String baseMetal) {
        this.baseMetal = baseMetal;
    }

    public String getNps() {
        return nps;
    }

    public void setNps(String nps) {
        this.nps = nps;
    }

    public String getSch() {
        return sch;
    }

    public void setSch(String sch) {
        this.sch = sch;
    }

    public String getMinThickness() {
        return minThickness;
    }

    public void setMinThickness(String minThickness) {
        this.minThickness = minThickness;
    }

    public String getContainMinThickness() {
        return containMinThickness;
    }

    public void setContainMinThickness(String containMinThickness) {
        this.containMinThickness = containMinThickness;
    }

    public String getMaxThickness() {
        return maxThickness;
    }

    public void setMaxThickness(String maxThickness) {
        this.maxThickness = maxThickness;
    }

    public String getContainMaxThickness() {
        return containMaxThickness;
    }

    public void setContainMaxThickness(String containMaxThickness) {
        this.containMaxThickness = containMaxThickness;
    }

    public String getMinDia() {
        return minDia;
    }

    public void setMinDia(String minDia) {
        this.minDia = minDia;
    }

    public String getContainMinDia() {
        return containMinDia;
    }

    public void setContainMinDia(String containMinDia) {
        this.containMinDia = containMinDia;
    }

    public String getMaxDia() {
        return maxDia;
    }

    public void setMaxDia(String maxDia) {
        this.maxDia = maxDia;
    }

    public String getContainMaxDia() {
        return containMaxDia;
    }

    public void setContainMaxDia(String containMaxDia) {
        this.containMaxDia = containMaxDia;
    }
}
