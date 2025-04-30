package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.material.TagNumberType;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * 余料库保存dto
 */
public class FCompanySurplusMaterialCreateDTO extends BaseDTO {
    private static final long serialVersionUID = -3548931677298658302L;

    private String qrCode;

    private Long qrCodeId;

    private String companyId;

    private String companyCode;

    private String materialBatchNumber;

    private BigDecimal qty;

    private int cnt;

    private int tagNumberId;

    // 材料编码
    private String tagNumber;

    private String ident;

    // 材料类型(一物一码，一类一码)
    private TagNumberType tagNumberType;

    // 材料规格
    private String spec;

    @Schema(description = "材质")
    private String texture;

    private String dpCode;

    private int dpId;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS 单位")
    private LengthUnit npsUnit;

    @Schema(description = "NPS")
    private Double nps;

    // 材料单位ID
    private int unitId;

    // 材料单位
    private String unitCode;

    // 炉批号ID
    private Long heatNoId;

    private String heatNoCode;

    // 流水号
    private int seqNumber;

    // 描述
    private String shortDesc;

    @Schema(description = "关联的套料材料明细ID")
    private Long relationshipNestMaterialItemId;

    @Schema(description = "关联的材料准备单ID")
    private Long relationshipMaterialPrepareId;


    public Long getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(Long qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public String getMaterialBatchNumber() {
        return materialBatchNumber;
    }

    public void setMaterialBatchNumber(String materialBatchNumber) {
        this.materialBatchNumber = materialBatchNumber;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public int getTagNumberId() {
        return tagNumberId;
    }

    public void setTagNumberId(int tagNumberId) {
        this.tagNumberId = tagNumberId;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public TagNumberType getTagNumberType() {
        return tagNumberType;
    }

    public void setTagNumberType(TagNumberType tagNumberType) {
        this.tagNumberType = tagNumberType;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Long getHeatNoId() {
        return heatNoId;
    }

    public void setHeatNoId(Long heatNoId) {
        this.heatNoId = heatNoId;
    }

    public String getHeatNoCode() {
        return heatNoCode;
    }

    public void setHeatNoCode(String heatNoCode) {
        this.heatNoCode = heatNoCode;
    }

    public int getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(int seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

    public Long getRelationshipNestMaterialItemId() {
        return relationshipNestMaterialItemId;
    }

    public void setRelationshipNestMaterialItemId(Long relationshipNestMaterialItemId) {
        this.relationshipNestMaterialItemId = relationshipNestMaterialItemId;
    }

    public Long getRelationshipMaterialPrepareId() {
        return relationshipMaterialPrepareId;
    }

    public void setRelationshipMaterialPrepareId(Long relationshipMaterialPrepareId) {
        this.relationshipMaterialPrepareId = relationshipMaterialPrepareId;
    }

    public String getDpCode() {
        return dpCode;
    }

    public void setDpCode(String dpCode) {
        this.dpCode = dpCode;
    }

    public int getDpId() {
        return dpId;
    }

    public void setDpId(int dpId) {
        this.dpId = dpId;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }
}
