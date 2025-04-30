package com.ose.tasks.entity.qc;

import com.ose.tasks.vo.bpm.ExInspStatus;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 管道专业 实施记录。
 */
@Entity
@Table(name = "structure_construction_log",
indexes = {
    @Index(columnList = "entityId,actInstId,deleted", unique = true),
    @Index(columnList = "actInstId,testResult,deleted"),
    @Index(columnList = "processId")
})
public final class StructureTestLog extends BaseConstructionLog {


    private static final long serialVersionUID = -1757318324286126108L;

    @Schema(description = "材料1 炉批号 Id")
    @Column
    private Long heatNoId1;

    @Schema(description = "材料2 炉批号Id")
    @Column
    private Long heatNoId2;

    @Schema(description = "焊后热处理编号")
    @Column
    private String pwhtCode;

    @Schema(description = "WPS Nos,逗号分隔")
    @Column
    private String wpsNos;

    @Schema(description = "外检结果")
    @Column
    @Enumerated(EnumType.STRING)
    private ExInspStatus exInspStatus;

    @Schema(description = "射线测试胶片质量")
    @Column
    private String rtFilmQuantity;

    @Schema(description = "探伤长度")
    @Column
    private Integer inspectionLength;

    @Schema(description = "主管")
    @Column
    private String supervisor;

    @Schema(description = "wp01 ID")
    @Column(name = "wp01_id")
    private Long wp01Id;

    @Schema(description = "wp02 ID")
    @Column(name = "wp02_id")
    private Long wp02Id;

    @Schema(description = "wp03 ID")
    @Column(name = "wp03_id")
    private Long wp03Id;

    @Schema(description = "wp04 ID")
    @Column(name = "wp04_id")
    private Long wp04Id;

    @Schema(description = "探伤缺陷长度")
    @Column
    private String flawLength;

    @Schema(description = "探伤缺陷长度单位")
    @Column
    @Enumerated(EnumType.STRING)
    private LengthUnit flawLengthUnit;

    @Schema(description = "返修次数")
    @Column
    private String weldRepairCount;

    @Schema(description = "焊材批号a")
    @Column
    private String weldMaterialNoA;

    @Schema(description = "焊材批次号71Ni")
    @Column
    private String weldMaterial71Ni;

    @Schema(description = "焊材批次号81K2")
    @Column
    private String weldMaterial81K2;

    @Schema(description = "焊材批号b")
    @Column
    private String weldMaterialNoB;

    @Schema(description = "焊材批号c")
    @Column
    private String weldMaterialNoC;

    @Schema(description = "焊材批号d")
    @Column
    private String weldMaterialNoD;

    @Schema(description = "焊材批号e")
    @Column
    private String weldMaterialNoE;

    @Schema(description = "缺陷类型")
    @Column
    private String defectType;

    @Schema(description = "ut缺陷类型")
    @Column
    private String utDefectType;

    @Schema(description = "ut缺陷长度")
    @Column
    private String defectSizeUt;

    @Schema(description = "rt缺陷长度")
    @Column
    private String defectSizeRt;

    @Schema(description = "paut缺陷长度")
    @Column
    private String defectSizePaut;

    @Schema(description = "缺陷位置")
    @Column
    private String defectPosition;

    @Schema(description = "下料材质1")
    @Column
    private String material1;

    @Schema(description = "下料材质2")
    @Column
    private String material2;

    @Schema(description = "是否为虚拟数据")
    @Column
    private Boolean manualFilled = false;

    public String getPwhtCode() {
        return pwhtCode;
    }

    public void setPwhtCode(String pwhtCode) {
        this.pwhtCode = pwhtCode;
    }

    public String getWpsNos() {
        return wpsNos;
    }

    public void setWpsNos(String wpsNos) {
        this.wpsNos = wpsNos;
    }

    public ExInspStatus getExInspStatus() {
        return exInspStatus;
    }

    public void setExInspStatus(ExInspStatus exInspStatus) {
        this.exInspStatus = exInspStatus;
    }

    public String getRtFilmQuantity() {
        return rtFilmQuantity;
    }

    public void setRtFilmQuantity(String rtFilmQuantity) {
        this.rtFilmQuantity = rtFilmQuantity;
    }

    public Integer getInspectionLength() {
        return inspectionLength;
    }

    public void setInspectionLength(Integer inspectionLength) {
        this.inspectionLength = inspectionLength;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public Long getWp04Id() {
        return wp04Id;
    }

    public void setWp04Id(Long wp04Id) {
        this.wp04Id = wp04Id;
    }

    public Long getWp01Id() {
        return wp01Id;
    }

    public void setWp01Id(Long wp01Id) {
        this.wp01Id = wp01Id;
    }

    public String getWeldMaterial71Ni() {
        return weldMaterial71Ni;
    }

    public void setWeldMaterial71Ni(String weldMaterial71Ni) {
        this.weldMaterial71Ni = weldMaterial71Ni;
    }

    public String getWeldMaterial81K2() {
        return weldMaterial81K2;
    }

    public void setWeldMaterial81K2(String weldMaterial81K2) {
        this.weldMaterial81K2 = weldMaterial81K2;
    }

    public Long getWp02Id() {
        return wp02Id;
    }

    public void setWp02Id(Long wp02Id) {
        this.wp02Id = wp02Id;
    }

    public Long getWp03Id() {
        return wp03Id;
    }

    public void setWp03Id(Long wp03Id) {
        this.wp03Id = wp03Id;
    }

    public Long getHeatNoId1() {
        return heatNoId1;
    }

    public void setHeatNoId1(Long heatNoId1) {
        this.heatNoId1 = heatNoId1;
    }

    public Long getHeatNoId2() {
        return heatNoId2;
    }

    public void setHeatNoId2(Long heatNoId2) {
        this.heatNoId2 = heatNoId2;
    }

    public String getFlawLength() {
        return flawLength;
    }

    public void setFlawLength(String flawLength) {
        this.flawLength = flawLength;
    }

    public LengthUnit getFlawLengthUnit() {
        return flawLengthUnit;
    }

    public void setFlawLengthUnit(LengthUnit flawLengthUnit) {
        this.flawLengthUnit = flawLengthUnit;
    }

    public String getWeldRepairCount() {
        return weldRepairCount;
    }

    public void setWeldRepairCount(String weldRepairCount) {
        this.weldRepairCount = weldRepairCount;
    }

    public String getWeldMaterialNoA() {
        return weldMaterialNoA;
    }

    public void setWeldMaterialNoA(String weldMaterialNoA) {
        this.weldMaterialNoA = weldMaterialNoA;
    }

    public String getWeldMaterialNoB() {
        return weldMaterialNoB;
    }

    public void setWeldMaterialNoB(String weldMaterialNoB) {
        this.weldMaterialNoB = weldMaterialNoB;
    }

    public String getWeldMaterialNoC() {
        return weldMaterialNoC;
    }

    public void setWeldMaterialNoC(String weldMaterialNoC) {
        this.weldMaterialNoC = weldMaterialNoC;
    }

    public String getWeldMaterialNoD() {
        return weldMaterialNoD;
    }

    public void setWeldMaterialNoD(String weldMaterialNoD) {
        this.weldMaterialNoD = weldMaterialNoD;
    }

    public String getWeldMaterialNoE() {
        return weldMaterialNoE;
    }

    public void setWeldMaterialNoE(String weldMaterialNoE) {
        this.weldMaterialNoE = weldMaterialNoE;
    }

    public String getDefectType() {
        return defectType;
    }

    public void setDefectType(String defectType) {
        this.defectType = defectType;
    }

    public String getDefectSizeUt() {
        return defectSizeUt;
    }

    public void setDefectSizeUt(String defectSizeUt) {
        this.defectSizeUt = defectSizeUt;
    }

    public String getDefectSizeRt() {
        return defectSizeRt;
    }

    public void setDefectSizeRt(String defectSizeRt) {
        this.defectSizeRt = defectSizeRt;
    }

    public String getDefectPosition() {
        return defectPosition;
    }

    public void setDefectPosition(String defectPosition) {
        this.defectPosition = defectPosition;
    }

    public String getDefectSizePaut() {
        return defectSizePaut;
    }

    public void setDefectSizePaut(String defectSizePaut) {
        this.defectSizePaut = defectSizePaut;
    }

    public String getUtDefectType() {
        return utDefectType;
    }

    public void setUtDefectType(String utDefectType) {
        this.utDefectType = utDefectType;
    }

    public String getMaterial1() {
        return material1;
    }

    public void setMaterial1(String material1) {
        this.material1 = material1;
    }

    public String getMaterial2() {
        return material2;
    }

    public void setMaterial2(String material2) {
        this.material2 = material2;
    }

    public Boolean getManualFilled() {
        return manualFilled;
    }

    public void setManualFilled(Boolean manualFilled) {
        this.manualFilled = manualFilled;
    }
}
