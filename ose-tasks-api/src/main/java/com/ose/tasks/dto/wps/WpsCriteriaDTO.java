package com.ose.tasks.dto.wps;

import com.ose.dto.PageDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class WpsCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -1632156962222912701L;

    //ftj
    @Schema(description = "连接方式")
    private String weldType;

    @Schema(description = "母材")
    private String baseMetal;

    //fftj
    @Schema(description = "母材2")
    private String baseMetal2;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "最小厚度")
    private String minThickness;

    @Schema(description = "是否包含最小厚度")
    private String containMinThickness;

    @Schema(description = "最大厚度")
    private String maxThickness;

    @Schema(description = "是否包含最大厚度")
    private String containMaxThickness;

    @Schema(description = "最小直径")
    private String minDiaRange;

    @Schema(description = "是否包含最小直径")
    private String containMinDiaRange;

    @Schema(description = "最大直径")
    private String maxDiaRange;

    @Schema(description = "是否包含最大直径")
    private String containMaxDiaRange;

    //ftj
    public String getWeldType() {
        return weldType;
    }

    //ftj
    public void setWeldType(String weldType) {
        this.weldType = weldType;
    }

    public String getBaseMetal() {
        return baseMetal;
    }

    public void setBaseMetal(String baseMetal) {
        this.baseMetal = baseMetal;
    }

    public String getBaseMetal2() {
        return baseMetal2;
    }

    public void setBaseMetal2(String baseMetal2) {
        this.baseMetal2 = baseMetal2;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getMinThickness() {
        return minThickness;
    }

    public void setMinThickness(String minThickness) {
        this.minThickness = minThickness;
    }

    public String getMaxThickness() {
        return maxThickness;
    }

    public void setMaxThickness(String maxThickness) {
        this.maxThickness = maxThickness;
    }

    public String getMinDiaRange() {
        return minDiaRange;
    }

    public void setMinDiaRange(String minDiaRange) {
        this.minDiaRange = minDiaRange;
    }

    public String getMaxDiaRange() {
        return maxDiaRange;
    }

    public void setMaxDiaRange(String maxDiaRange) {
        this.maxDiaRange = maxDiaRange;
    }

    public String getContainMinThickness() {
        return containMinThickness;
    }

    public void setContainMinThickness(String containMinThickness) {
        this.containMinThickness = containMinThickness;
    }

    public String getContainMaxThickness() {
        return containMaxThickness;
    }

    public void setContainMaxThickness(String containMaxThickness) {
        this.containMaxThickness = containMaxThickness;
    }

    public String getContainMinDiaRange() {
        return containMinDiaRange;
    }

    public void setContainMinDiaRange(String containMinDiaRange) {
        this.containMinDiaRange = containMinDiaRange;
    }

    public String getContainMaxDiaRange() {
        return containMaxDiaRange;
    }

    public void setContainMaxDiaRange(String containMaxDiaRange) {
        this.containMaxDiaRange = containMaxDiaRange;
    }
}
