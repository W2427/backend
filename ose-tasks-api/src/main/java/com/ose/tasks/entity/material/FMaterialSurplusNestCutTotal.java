package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.vo.material.MaterialLockedStatus;
import com.ose.tasks.vo.material.NestingState;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "mat_f_material_surplus_nest_cut_total")
public class FMaterialSurplusNestCutTotal extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "余料套料清单id")
    private Long fMaterialSurplusNestId;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "NPS")
    private Double nps;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS 单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private LengthUnit npsUnit;

    @Schema(description = "总长度")
    @Column(precision = 19, scale = 3)
    private BigDecimal totalLength;

    @Schema(description = "材料描述")
    private String materialDesc;

    @Schema(description = "材料锁定状态")
    @Enumerated(EnumType.STRING)
    private MaterialLockedStatus materialLockStatus;

    @Schema(description = "材料锁定表的id")
    private Long materialLockId;

    @Schema(description = "套料状态")
    @Enumerated(EnumType.STRING)
    private NestingState nestStatus;

    @Schema(description = "运行时间")
    private Long executionTime;

    @Schema(description = "余料总长度")
    @Column(precision = 19, scale = 3)
    private BigDecimal surplusSumLength;

    @Schema(description = "用料总长度")
    @Column(precision = 19, scale = 3)
    private BigDecimal materialSumLength;

    @Schema(description = "切割总长度")
    @Column(precision = 19, scale = 3)
    private BigDecimal cutSumLength;

    @Schema(description = "废料总长度")
    @Column(precision = 19, scale = 3)
    private BigDecimal scrapSumLength;

    @Schema(description = "")
    private String memo;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public Double getNps() {
        return nps;
    }

    public void setNps(Double nps) {
        this.nps = nps;
    }

    public String getNpsText() {
        return npsText;
    }

    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    public LengthUnit getNpsUnit() {
        return npsUnit;
    }

    public void setNpsUnit(LengthUnit npsUnit) {
        this.npsUnit = npsUnit;
    }

    public BigDecimal getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(BigDecimal totalLength) {
        this.totalLength = totalLength;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public MaterialLockedStatus getMaterialLockStatus() {
        return materialLockStatus;
    }

    public void setMaterialLockStatus(MaterialLockedStatus materialLockStatus) {
        this.materialLockStatus = materialLockStatus;
    }

    public Long getMaterialLockId() {
        return materialLockId;
    }

    public void setMaterialLockId(Long materialLockId) {
        this.materialLockId = materialLockId;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
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

    public void addLength(Double materialLength) {
        this.totalLength = this.totalLength.add(BigDecimal.valueOf(materialLength));
    }

    public NestingState getNestStatus() {
        return nestStatus;
    }

    public void setNestStatus(NestingState nestStatus) {
        this.nestStatus = nestStatus;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getfMaterialSurplusNestId() {
        return fMaterialSurplusNestId;
    }

    public void setfMaterialSurplusNestId(Long fMaterialSurplusNestId) {
        this.fMaterialSurplusNestId = fMaterialSurplusNestId;
    }
}
