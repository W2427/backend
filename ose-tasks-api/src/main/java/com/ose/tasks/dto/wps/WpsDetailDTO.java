package com.ose.tasks.dto.wps;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class WpsDetailDTO extends BaseDTO {

    private static final long serialVersionUID = 2171488475666621704L;

    @Schema(description = "ID【非必须，更新时使用】")
    private String id;

    @Schema(description = "母材A")
    private List<String> baseMetalA;

    @Schema(description = "母材A别名")
    private List<String> baseMetalAAlias;

    @Schema(description = "母材B")
    private List<String> baseMetalB;

    @Schema(description = "母材B别名")
    private List<String> baseMetalBAlias;

    @Schema(description = "填充材料")
    private List<String> fillerMetal;

    @Schema(description = "接缝方式")
    private String joint;

    @Schema(description = "是否清根")
    private Boolean gouging;

    @Schema(description = "是否有衬垫")
    private Boolean backing;

    @Schema(description = "焊接位置")
    private List<String> position;

    @Schema(description = "坡口类型")
    private String bevelType;

    @Schema(description = "最大坡口角度")
    private Double maxGrooveAngle;

    @Schema(description = "最小坡口角度")
    private Double minGrooveAngle;

    @Schema(description = "坡口图片")
    private String groovePhoto;

    @Schema(description = "厚度")
    private String thickness;

    @Schema(description = "厚度附加信息")
    private String thicknessAdditionalInfo;

    @Schema(description = "是否包含最大厚度")
    private boolean containMaxThickness;

    @Schema(description = "是否包含最小厚度")
    private boolean containMinThickness;

    @Schema(description = "最大适用厚度")
    private Double maxThickness;

    @Schema(description = "最小适用厚度")
    private Double minThickness;

    @Schema(description = "适用直径")
    private String diaRange;

    @Schema(description = "最大适用直径")
    private Double maxDiaRange;

    @Schema(description = "是否包含最大适用直径")
    private boolean containMaxDiaRange;

    @Schema(description = "最小适用直径")
    private Double minDiaRange;

    @Schema(description = "是否包含最小适用直径")
    private boolean containMinDiaRange;

    @Schema(description = "PWHT")
    private String pwht;

    @Schema(description = "采用标准")
    private String standard;

    @Schema(description = "认证方")
    private String authenticator;

    @Schema(description = "适用规范章节")
    private String volume;

    @Schema(description = "文件 ID")
    private Long fileId;

    public List<String> getBaseMetalA() {
        return baseMetalA;
    }

    public void setBaseMetalA(List<String> baseMetalA) {
        this.baseMetalA = baseMetalA;
    }

    public List<String> getBaseMetalAAlias() {
        return baseMetalAAlias;
    }

    public void setBaseMetalAAlias(List<String> baseMetalAAlias) {
        this.baseMetalAAlias = baseMetalAAlias;
    }

    public List<String> getBaseMetalB() {
        return baseMetalB;
    }

    public void setBaseMetalB(List<String> baseMetalB) {
        this.baseMetalB = baseMetalB;
    }

    public List<String> getBaseMetalBAlias() {
        return baseMetalBAlias;
    }

    public void setBaseMetalBAlias(List<String> baseMetalBAlias) {
        this.baseMetalBAlias = baseMetalBAlias;
    }

    public List<String> getFillerMetal() {
        return fillerMetal;
    }

    public void setFillerMetal(List<String> fillerMetal) {
        this.fillerMetal = fillerMetal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJoint() {
        return joint;
    }

    public void setJoint(String joint) {
        this.joint = joint;
    }

    public Boolean getGouging() {
        return gouging;
    }

    public void setGouging(Boolean gouging) {
        this.gouging = gouging;
    }

    public Boolean getBacking() {
        return backing;
    }

    public void setBacking(Boolean backing) {
        this.backing = backing;
    }

    public List<String> getPosition() {
        return position;
    }

    public void setPosition(List<String> position) {
        this.position = position;
    }

    public String getBevelType() {
        return bevelType;
    }

    public void setBevelType(String bevelType) {
        this.bevelType = bevelType;
    }

    public Double getMaxGrooveAngle() {
        return maxGrooveAngle;
    }

    public void setMaxGrooveAngle(Double maxGrooveAngle) {
        this.maxGrooveAngle = maxGrooveAngle;
    }

    public Double getMinGrooveAngle() {
        return minGrooveAngle;
    }

    public void setMinGrooveAngle(Double minGrooveAngle) {
        this.minGrooveAngle = minGrooveAngle;
    }

    public String getGroovePhoto() {
        return groovePhoto;
    }

    public void setGroovePhoto(String groovePhoto) {
        this.groovePhoto = groovePhoto;
    }

    public Double getMaxThickness() {
        return maxThickness;
    }

    public void setMaxThickness(Double maxThickness) {
        this.maxThickness = maxThickness;
    }

    public Double getMinThickness() {
        return minThickness;
    }

    public void setMinThickness(Double minThickness) {
        this.minThickness = minThickness;
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    public String getThicknessAdditionalInfo() {
        return thicknessAdditionalInfo;
    }

    public void setThicknessAdditionalInfo(String thicknessAdditionalInfo) {
        this.thicknessAdditionalInfo = thicknessAdditionalInfo;
    }

    public boolean isContainMaxThickness() {
        return containMaxThickness;
    }

    public void setContainMaxThickness(boolean containMaxThickness) {
        this.containMaxThickness = containMaxThickness;
    }

    public boolean isContainMinThickness() {
        return containMinThickness;
    }

    public void setContainMinThickness(boolean containMinThickness) {
        this.containMinThickness = containMinThickness;
    }

    public Double getMaxDiaRange() {
        return maxDiaRange;
    }

    public void setMaxDiaRange(Double maxDiaRange) {
        this.maxDiaRange = maxDiaRange;
    }

    public Double getMinDiaRange() {
        return minDiaRange;
    }

    public void setMinDiaRange(Double minDiaRange) {
        this.minDiaRange = minDiaRange;
    }

    public String getDiaRange() {
        return diaRange;
    }

    public void setDiaRange(String diaRange) {
        this.diaRange = diaRange;
    }

    public boolean isContainMaxDiaRange() {
        return containMaxDiaRange;
    }

    public void setContainMaxDiaRange(boolean containMaxDiaRange) {
        this.containMaxDiaRange = containMaxDiaRange;
    }

    public boolean isContainMinDiaRange() {
        return containMinDiaRange;
    }

    public void setContainMinDiaRange(boolean containMinDiaRange) {
        this.containMinDiaRange = containMinDiaRange;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(String authenticator) {
        this.authenticator = authenticator;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getPwht() {
        return pwht;
    }

    public void setPwht(String pwht) {
        this.pwht = pwht;
    }
}
