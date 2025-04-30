package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.dto.BaseDTO;
import com.ose.vo.EntityStatus;
import com.ose.vo.unit.LengthUnit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Pipe-Piece实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PipePieceEntryUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = 3753812239306672043L;

    @Schema(description = "版本号")
    private String revision;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "弯管#1代码")
    private String bevelCode1;

    @Schema(description = "弯管#2代码")
    private String bevelCode2;

    @Schema(description = "弯管信息")
    private String bendInfo;

    @Schema(description = "下料图纸")
    private String cutDrawing;

    @Schema(description = "材质代码")
    private String materialCode;

    @Schema(description = "材质描述")
    private String material;

    @Schema(description = "NPS 单位")
    @Enumerated(EnumType.STRING)
    private LengthUnit npsUnit;

    @Schema(description = "NPS")
    private Double nps;

    @Schema(description = "长度单位")
    @Enumerated(EnumType.STRING)
    private LengthUnit lengthUnit;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "长度")
    private Double length;

    @Schema(description = "长度表示值")
    private String lengthText;

    @Schema(description = "管道等级")
    private String pipeClass;

    @Schema(description = "批量删除标记")
    private String remarks2;

    // 数据实体状态
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    // 是否已被删除
    private boolean deleted = false;

    // 更新版本（手动乐观锁）
    private long version;

    @JsonCreator
    public PipePieceEntryUpdateDTO() {
    }

    public String getBevelCode1() {
        return bevelCode1;
    }

    public void setBevelCode1(String bevelCode1) {
        this.bevelCode1 = bevelCode1;
    }

    public String getBevelCode2() {
        return bevelCode2;
    }

    public void setBevelCode2(String bevelCode2) {
        this.bevelCode2 = bevelCode2;
    }

    public String getBendInfo() {
        return bendInfo;
    }

    public void setBendInfo(String bendInfo) {
        this.bendInfo = bendInfo;
    }

    public String getCutDrawing() {
        return cutDrawing;
    }

    public void setCutDrawing(String cutDrawing) {
        this.cutDrawing = cutDrawing;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public LengthUnit getNpsUnit() {
        return npsUnit;
    }

    @JsonSetter
    public void setNpsUnit(LengthUnit npsUnit) {
        this.npsUnit = npsUnit;
    }

    @JsonSetter
    public void setNpsUnit(String npsUnit) {
        this.npsUnit = LengthUnit.getByName(npsUnit);
    }

    public Double getNps() {
        return nps;
    }

    @JsonSetter
    public void setNps(Double nps) {
        this.nps = nps;
    }

    public String getNpsText() {
        return npsText;
    }

    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    public LengthUnit getLengthUnit() {
        return lengthUnit;
    }

    @JsonSetter
    public void setLengthUnit(LengthUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    @JsonSetter
    public void setLengthUnit(String lengthUnit) {
        this.lengthUnit = LengthUnit.getByName(lengthUnit);
    }

    public Double getLength() {
        return length;
    }

    @JsonSetter
    public void setLength(Double length) {
        this.length = length;
    }

    public String getLengthText() {
        return lengthText;
    }

    public void setLengthText(String lengthText) {
        this.lengthText = lengthText;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    /**
     * 取得是否已被删除的标记。
     *
     * @return 是否已被删除
     */
    public boolean getDeleted() {
        return deleted;
    }

    /**
     * 设置是否已被删除的标记。
     *
     * @param deleted 是否已被删除
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * 取得更新版本。
     *
     * @return 更新版本
     */
    public long getVersion() {
        return version;
    }

    /**
     * 设置更新版本。
     *
     * @param version 更新版本
     */
    public void setVersion(long version) {
        this.version = version;
    }

    /**
     * 取得状态。
     *
     * @return 状态
     */
    public EntityStatus getStatus() {
        return status;
    }

    /**
     * 设置状态。
     *
     * @param status 状态
     */
    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPipeClass() {
        return pipeClass;
    }

    public void setPipeClass(String pipeClass) {
        this.pipeClass = pipeClass;
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }
}
