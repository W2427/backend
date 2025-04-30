package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.vo.material.TagNumberType;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 施工单位余料主表实体
 */
@Entity
@Table(name = "mat_f_company_surplus_material")
public class FCompanySurplusMaterialsEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -2934156090959025713L;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "company_code")
    private String companyCode;

    // 专业ID
    @Column(name = "dp_id")
    private int dpId;

    // 专业
    @Column(name = "dp_code")
    private String dpCode;

    // 材料编码ID
    @Column(name = "tag_number_id")
    private int tagNumberId;

    // 材料编码
    @Column(name = "tag_number")
    private String tagNumber;

    private String ident;

    // 材料类型(一物一码，一类一码)
    @Column(name = "tag_number_type")
    @Enumerated(EnumType.STRING)
    private TagNumberType tagNumberType;

    // 材料规格
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

    // 材料单位ID
    @Column(name = "unit_id")
    private int unitId;

    // 材料单位
    @Column(name = "unit_code")
    private String unitCode;

    // 炉批号ID
    @Column(name = "heat_no_id")
    private Long heatNoId;

    private String heatNoCode;

    // 流水号
    @Column(name = "seq_number")
    private int seqNumber;

    // 描述
    @Column(name = "short_desc",length = 500)
    private String shortDesc;

    @Schema(description = "接收数量")
    @Column(name = "recv_qty", precision = 19, scale = 3)
    private BigDecimal recvQty;

    @Schema(description = "使用数量")
    @Column(name = "used_qty", precision = 19, scale = 3)
    private BigDecimal usedQty;

    @Schema(description = "退库数量")
    @Column(name = "return_qty", precision = 19, scale = 3)
    private BigDecimal returnQty;

    @Schema(description = "")
    @Column(name = "locked_qty", precision = 19, scale = 3)
    private BigDecimal lockedQty;

    @Schema(description = "余料数量")
    @Column(name = "surplus_qty", precision = 19, scale = 3)
    private BigDecimal surplusQty;

    public BigDecimal getLockedQty() {
        return lockedQty;
    }

    public void setLockedQty(BigDecimal lockedQty) {
        this.lockedQty = lockedQty;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

    public BigDecimal getRecvQty() {
        return recvQty;
    }

    public void setRecvQty(BigDecimal recvQty) {
        this.recvQty = recvQty;
    }

    public BigDecimal getUsedQty() {
        return usedQty;
    }

    public void setUsedQty(BigDecimal usedQty) {
        this.usedQty = usedQty;
    }

    public BigDecimal getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(BigDecimal returnQty) {
        this.returnQty = returnQty;
    }

    public BigDecimal getSurplusQty() {
        return surplusQty;
    }

    public void setSurplusQty(BigDecimal surplusQty) {
        this.surplusQty = surplusQty;
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
