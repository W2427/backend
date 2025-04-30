package com.ose.tasks.entity.material;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.material.OSDType;
import com.ose.tasks.vo.material.OpenBoxInspectionResultType;
import com.ose.tasks.vo.material.TagNumberType;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 材料盘点ITEM实体类。
 */
@Entity
@Table(name = "mat_f_material_stocktake_item")
public class FMaterialStocktakeItemEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "fmst_id")
    private Long fmstId;

    @Column(name = "dp_id")
    private int dpId;

    @Column(name = "dp_code")
    private String dpCode;

    @Column(name = "heat_no_id")
    private Long heatNoId;

    @Column(name = "cert_id")
    private String certId;

    @Column(name = "heat_no_code")
    private String heatNoCode;

    @Column(name = "tag_number_id")
    private int tagNumberId;

    @Column(name = "tag_number")
    private String tagNumber;

    private String ident;

    @Column(name = "tag_number_type")
    @Enumerated(EnumType.STRING)
    private TagNumberType tagNumberType;

    @Column(name = "unit_id")
    private int unitId;

    @Column(name = "unit_code")
    private String unitCode;

    @Column(name = "spec")
    private String spec;

    @Schema(description = "材质")
    private String texture;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS 单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private LengthUnit npsUnit;

    @Schema(description = "NPS")
    private Double nps;

    @Column(name = "short_desc", length = 500)
    private String shortDesc;

    @Schema(description = "单位量")
    @Column(name = "qty", precision = 19, scale = 3)
    private BigDecimal qty = BigDecimal.ZERO;

    @Schema(description = "盘点")
    @Column(name = "qty_cnt")
    private int qtyCnt = 0;

    @Schema(description = "损坏数量")
    private int damageQtyCnt = 0;

    @Schema(description = "内检质量问题数量")
    private int internalInspectionQtyCnt = 0;

    @Schema(description = "外检质量问题数量")
    private int externalInspectionQtyCnt = 0;

    @Schema(description = "单个材料的合计量")
    @Column(precision = 19, scale = 3)
    private BigDecimal itemTotalQty = BigDecimal.ZERO;

    @Column(name = "qr_code")
    private String qrCode;

    @Column(name = "qr_code_id")
    private Long qrCodeId;

    @Column(name = "reln_id")
    private Long relnId;

    @Column(name = "reln_item_id")
    private Long relnItemId;

    @Column(name = "internal_inspection_result")
    @Enumerated(EnumType.STRING)
    private OpenBoxInspectionResultType internalInspectionResult;

    @Column(name = "external_inspection_result")
    @Enumerated(EnumType.STRING)
    private OpenBoxInspectionResultType externalInspectionResult;

    @Column(name = "osd_type")
    @Enumerated(EnumType.STRING)
    private OSDType osdType;

    @Schema(description = "备注")
    private String memo;

    @Schema(description = "仓库ID")
    private String whId;

    @Schema(description = "仓库")
    private String whCode;

    @Schema(description = "库位ID")
    private String locId;

    @Schema(description = "库位")
    private String locCode;

    @Schema(description = "货架-层号")
    private String goodsShelf;

    public String getWhId() {
        return whId;
    }

    public void setWhId(String whId) {
        this.whId = whId;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getGoodsShelf() {
        return goodsShelf;
    }

    public void setGoodsShelf(String goodsShelf) {
        this.goodsShelf = goodsShelf;
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

    public Long getFmstId() {
        return fmstId;
    }

    public void setFmstId(Long fmstId) {
        this.fmstId = fmstId;
    }

    public int getDpId() {
        return dpId;
    }

    public void setDpId(int dpId) {
        this.dpId = dpId;
    }

    public String getDpCode() {
        return dpCode;
    }

    public void setDpCode(String dpCode) {
        this.dpCode = dpCode;
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

    public TagNumberType getTagNumberType() {
        return tagNumberType;
    }

    public void setTagNumberType(TagNumberType tagNumberType) {
        this.tagNumberType = tagNumberType;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public int getQtyCnt() {
        return qtyCnt;
    }

    public void setQtyCnt(int qtyCnt) {
        this.qtyCnt = qtyCnt;
    }

    public int getDamageQtyCnt() {
        return damageQtyCnt;
    }

    public void setDamageQtyCnt(int damageQtyCnt) {
        this.damageQtyCnt = damageQtyCnt;
    }

    public int getInternalInspectionQtyCnt() {
        return internalInspectionQtyCnt;
    }

    public void setInternalInspectionQtyCnt(int internalInspectionQtyCnt) {
        this.internalInspectionQtyCnt = internalInspectionQtyCnt;
    }

    public int getExternalInspectionQtyCnt() {
        return externalInspectionQtyCnt;
    }

    public void setExternalInspectionQtyCnt(int externalInspectionQtyCnt) {
        this.externalInspectionQtyCnt = externalInspectionQtyCnt;
    }

    public BigDecimal getItemTotalQty() {
        return itemTotalQty;
    }

    public void setItemTotalQty(BigDecimal itemTotalQty) {
        this.itemTotalQty = itemTotalQty;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Long getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(Long qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public OpenBoxInspectionResultType getInternalInspectionResult() {
        return internalInspectionResult;
    }

    public void setInternalInspectionResult(OpenBoxInspectionResultType internalInspectionResult) {
        this.internalInspectionResult = internalInspectionResult;
    }

    public OpenBoxInspectionResultType getExternalInspectionResult() {
        return externalInspectionResult;
    }

    public void setExternalInspectionResult(OpenBoxInspectionResultType externalInspectionResult) {
        this.externalInspectionResult = externalInspectionResult;
    }

    public OSDType getOsdType() {
        return osdType;
    }

    public void setOsdType(OSDType osdType) {
        this.osdType = osdType;
    }

    public Long getRelnId() {
        return relnId;
    }

    public void setRelnId(Long relnId) {
        this.relnId = relnId;
    }

    public Long getRelnItemId() {
        return relnItemId;
    }

    public void setRelnItemId(Long relnItemId) {
        this.relnItemId = relnItemId;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void calculateItemTotalQty() {
        int totalQty = this.qtyCnt - this.damageQtyCnt - this.internalInspectionQtyCnt - this.externalInspectionQtyCnt;
        this.itemTotalQty = this.qty.multiply(new BigDecimal(totalQty));
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

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }
}
