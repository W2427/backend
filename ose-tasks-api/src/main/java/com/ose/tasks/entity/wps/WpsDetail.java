package com.ose.tasks.entity.wps;

import com.ose.entity.BaseVersionedBizEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "wps_detail")
public class WpsDetail extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 5675915927793735207L;

    @Column
    private Long wpsId;

    @Column(length = 512)
    private String baseMetalA;


    @Column(length = 1000)
    private String baseMetalAStr;

    @Column(length = 1600)
    private String baseMetalAAlias;

    @Column(length = 1600)
    private String baseMetalAAliasStr;

    @Column(length = 512)
    private String baseMetalB;


    @Column(length = 1000)
    private String baseMetalBStr;

    @Column(length = 1600)
    private String baseMetalBAlias;

    @Column(length = 1600)
    private String baseMetalBAliasStr;

    @Column(length = 255)
    private String fillerMetal;

    @Column(length = 160)
    private String joint;

    @Column
    private Boolean gouging;

    @Column
    private Boolean backing;

    @Column(length = 160)
    private String position;

    @Column(length = 160)
    private String bevelType;

    @Column(length = 16, nullable = false)
    private Long projectId;

    @Column(length = 16, nullable = false)
    private Long orgId;

    @Column
    private Double maxGrooveAngle;

    @Column
    private Double minGrooveAngle;

    @Column
    private Long groovePhoto;

    @Column
    private String thickness;

    @Column
    private String thicknessAdditionalInfo;

    @Column
    private Double maxThickness;

    @Column
    private boolean containMaxThickness = true;

    @Column
    private Double minThickness;

    @Column
    private boolean containMinThickness = true;

    @Column
    private String diaRange;

    @Column
    private Double maxDiaRange;

    @Column
    private boolean containMaxDiaRange = true;

    @Column
    private Double minDiaRange;

    @Column
    private boolean containMinDiaRange = true;

    @Column
    private String standard;

    @Column
    private String pwht;

    @Column
    private String authenticator;

    @Column
    private String volume;

    @Column
    private Long fileId;

    public void setBaseMetalA(String baseMetalA) {
        this.baseMetalA = baseMetalA;
    }

    public void setBaseMetalAAlias(String baseMetalAAlias) {
        this.baseMetalAAlias = baseMetalAAlias;
    }

    public void setBaseMetalB(String baseMetalB) {
        this.baseMetalB = baseMetalB;
    }

    public void setBaseMetalBAlias(String baseMetalBAlias) {
        this.baseMetalBAlias = baseMetalBAlias;
    }

    public void setFillerMetal(String fillerMetal) {
        this.fillerMetal = fillerMetal;
    }

    public Long getWpsId() {
        return wpsId;
    }

    public void setWpsId(Long wpsId) {
        this.wpsId = wpsId;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
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

    public Long getGroovePhoto() {
        return groovePhoto;
    }

    public void setGroovePhoto(Long groovePhoto) {
        this.groovePhoto = groovePhoto;
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

    public String getDiaRange() {
        return diaRange;
    }

    public void setDiaRange(String diaRange) {
        this.diaRange = diaRange;
    }

    public Double getMaxDiaRange() {
        return maxDiaRange;
    }

    public void setMaxDiaRange(Double maxDiaRange) {
        this.maxDiaRange = maxDiaRange;
    }

    public boolean isContainMaxDiaRange() {
        return containMaxDiaRange;
    }

    public void setContainMaxDiaRange(boolean containMaxDiaRange) {
        this.containMaxDiaRange = containMaxDiaRange;
    }

    public Double getMinDiaRange() {
        return minDiaRange;
    }

    public void setMinDiaRange(Double minDiaRange) {
        this.minDiaRange = minDiaRange;
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

    public String getBaseMetalA() {
        return baseMetalA;
    }

    public String getBaseMetalAAlias() {
        return baseMetalAAlias;
    }

    public String getBaseMetalB() {
        return baseMetalB;
    }

    public String getBaseMetalBAlias() {
        return baseMetalBAlias;
    }

    public String getFillerMetal() {
        return fillerMetal;
    }

    public String getPwht() {
        return pwht;
    }

    public void setPwht(String pwht) {
        this.pwht = pwht;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getBaseMetalAStr() {
        return baseMetalAStr;
    }

    public void setBaseMetalAStr(String baseMetalAStr) {
        this.baseMetalAStr = baseMetalAStr;
    }

    public String getBaseMetalAAliasStr() {
        return baseMetalAAliasStr;
    }

    public void setBaseMetalAAliasStr(String baseMetalAAliasStr) {
        this.baseMetalAAliasStr = baseMetalAAliasStr;
    }

    public String getBaseMetalBStr() {
        return baseMetalBStr;
    }

    public void setBaseMetalBStr(String baseMetalBStr) {
        this.baseMetalBStr = baseMetalBStr;
    }

    public String getBaseMetalBAliasStr() {
        return baseMetalBAliasStr;
    }

    public void setBaseMetalBAliasStr(String baseMetalBAliasStr) {
        this.baseMetalBAliasStr = baseMetalBAliasStr;
    }
}
