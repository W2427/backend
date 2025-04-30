package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.dto.numeric.LengthDTO;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 放行单物品关联炉批号实体类。
 */
@Entity
@Table(name = "mat_release_note_item_detail",
        indexes = {
        @Index(columnList = "heatNoId")
        })
public class ReleaseNoteItemDetailEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "放行单表ID")
    private Long relnId;

    @Schema(description = "放行单详情表ID")
    private Long relnItemId;

    @Schema(description = "炉批号ID")
    private Long heatNoId;

    @Schema(description = "材料规格")
    private String spec;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS 单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private LengthUnit npsUnit;

    @Schema(description = "NPS")
    private Double nps;

    @Schema(description = "单位量")
    @Column(precision = 19, scale = 3)
    private BigDecimal qty;

    @Schema(description = "单位")
    private String qtyUnitCode;

    @Schema(description = "件数")
    private int qtyCnt;

    @Schema(description = "材质证书ID")
    private Long certId;

    @Schema(description = "材质证书编号")
    private String certCode;

    @Schema(description = "证书页码")
    private String certPageNumber;

    @Schema(description = "材料名称")
    private String materialName;

    @Schema(description = "材质")
    private String texture;

    @Schema(description = "材质组别")
    private String materialGroup;

    @Schema(description = "库管员")
    private String storekeeper;

    @Schema(description = "重量")
    private String weight;

    @Schema(description = "生产厂家")
    private String manufacturer;

    @Schema(description = "批次号")
    private String batchNumber;

    @Schema(description = "追溯码（管附件）")
    private String traceCode;

    @Schema(description = "位号（阀件）")
    private String valveNumber;

    @Schema(description = "显示用单位长度")
    private String displayQty;

    @Schema(description = "显示用单位")
    private String displayQtyUnitCode;

    public Long getRelnItemId() {
        return relnItemId;
    }

    public void setRelnItemId(Long relnItemId) {
        this.relnItemId = relnItemId;
    }

    public Long getHeatNoId() {
        return heatNoId;
    }

    public void setHeatNoId(Long heatNoId) {
        this.heatNoId = heatNoId;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getQtyUnitCode() {
        return qtyUnitCode;
    }

    public void setQtyUnitCode(String qtyUnitCode) {
        this.qtyUnitCode = qtyUnitCode;
    }

    public int getQtyCnt() {
        return qtyCnt;
    }

    public void setQtyCnt(int qtyCnt) {
        this.qtyCnt = qtyCnt;
    }

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

    public Long getRelnId() {
        return relnId;
    }

    public void setRelnId(Long relnId) {
        this.relnId = relnId;
    }

    public Long getCertId() {
        return certId;
    }

    public void setCertId(Long certId) {
        this.certId = certId;
    }

    public String getCertCode() {
        return certCode;
    }

    public void setCertCode(String certCode) {
        this.certCode = certCode;
    }

    public String getCertPageNumber() {
        return certPageNumber;
    }

    public void setCertPageNumber(String certPageNumber) {
        this.certPageNumber = certPageNumber;
    }

    @Schema(description = "炉批号信息")
    @JsonProperty(value = "heatNoId", access = READ_ONLY)
    public ReferenceData getHeatNoIdRef() {
        return this.heatNoId == null
            ? null
            : new ReferenceData(this.heatNoId);
    }

    @Schema(description = "材质证书信息")
    @JsonProperty(value = "certId", access = READ_ONLY)
    public ReferenceData getCertIdRef() {
        return this.certId == null
            ? null
            : new ReferenceData(this.certId);
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getTraceCode() {
        return traceCode;
    }

    public void setTraceCode(String traceCode) {
        this.traceCode = traceCode;
    }

    public String getValveNumber() {
        return valveNumber;
    }

    public void setValveNumber(String valveNumber) {
        this.valveNumber = valveNumber;
    }

    public String getDisplayQty() {
        return displayQty;
    }

    public void setDisplayQty(String displayQty) {
        this.displayQty = displayQty;
    }

    public String getDisplayQtyUnitCode() {
        return displayQtyUnitCode;
    }

    public void setDisplayQtyUnitCode(String displayQtyUnitCode) {
        this.displayQtyUnitCode = displayQtyUnitCode;
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

    public Double getNps() {
        return nps;
    }

    public void setNps(Double nps) {
        this.nps = nps;
    }

    /**
     * 设定nps值。
     *
     * @param nps nps值
     */
    public void setNps(String nps) {
        this.npsText = nps;
        LengthDTO lengthDTO = new LengthDTO(this.npsUnit, nps);
        this.npsUnit = lengthDTO.getUnit();
        BigDecimal mm = BigDecimal.valueOf(lengthDTO.getMillimeters());
        this.nps = mm.divide(
            BigDecimal.valueOf(LengthDTO.EQUIVALENCES.get("inches")), 3, RoundingMode.CEILING).doubleValue();
    }

    public String getStorekeeper() {
        return storekeeper;
    }

    public void setStorekeeper(String storekeeper) {
        this.storekeeper = storekeeper;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMaterialGroup() {
        return materialGroup;
    }

    public void setMaterialGroup(String materialGroup) {
        this.materialGroup = materialGroup;
    }
}
