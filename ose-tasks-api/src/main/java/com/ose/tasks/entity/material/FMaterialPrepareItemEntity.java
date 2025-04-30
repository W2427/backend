package com.ose.tasks.entity.material;

import com.ose.entity.BaseBizEntity;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 材料准备单详情表
 */
@Entity
@Table(name = "mat_f_material_prepare_item")
public class FMaterialPrepareItemEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "材料准备详情说明")
    private String mpdCode;

    @Schema(description = "准备单ID")
    private Long mpId;

    @Schema(description = "准备单节点ID")
    private Long mpnId;

    @Schema(description = "管段id")
    private Long pipePieceId;

    @Schema(description = "准备单合计信息ID")
    private Long mpTotalId;

    @Schema(description = "材料编码")
    private String materialCode;

    @Schema(description = "材料节点编号")
    private String materialNo;

    @Schema(description = "材料NPS")
    private Double materialNps;

    @Schema(description = "材料NPS 表示值")
    private String materialNpsText;

    @Schema(description = "NPS 单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private LengthUnit materialNpsUnit;

    @Schema(description = "材料长度")
    private Double materialLength;

    @Schema(description = "材料长度 表示值")
    private String materialLengthText;

    @Schema(description = "材料规格，如2寸等")
    private String materialSpec;

    @Schema(description = "材料描述")
    private String material;

    @Schema(description = "实体类型 (PIPE_PIECE: 管段, COMPONENT:组件)")

    private String entityType;

    @Schema(description = "单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private LengthUnit unitCode;

    @Schema(description = "数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal qty;

    @Schema(description = "数量")
    private String qtyDisplay;

    @Schema(description = "准备数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal prepareQty;

    @Schema(description = "材料接收地点，为配送做准备")
    private String recvSite;

    @Schema(description = "材料接收人")
    private String recvPerson;

    @Transient
    private boolean isUsed = false;

    public Long getPipePieceId() {
        return pipePieceId;
    }

    public void setPipePieceId(Long pipePieceId) {
        this.pipePieceId = pipePieceId;
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

    public String getMpdCode() {
        return mpdCode;
    }

    public void setMpdCode(String mpdCode) {
        this.mpdCode = mpdCode;
    }

    public Long getMpId() {
        return mpId;
    }

    public void setMpId(Long mpId) {
        this.mpId = mpId;
    }

    public Long getMpnId() {
        return mpnId;
    }

    public void setMpnId(Long mpnId) {
        this.mpnId = mpnId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialSpec() {
        return materialSpec;
    }

    public void setMaterialSpec(String materialSpec) {
        this.materialSpec = materialSpec;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public LengthUnit getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(LengthUnit unitCode) {
        this.unitCode = unitCode;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getRecvSite() {
        return recvSite;
    }

    public void setRecvSite(String recvSite) {
        this.recvSite = recvSite;
    }

    public String getRecvPerson() {
        return recvPerson;
    }

    public void setRecvPerson(String recvPerson) {
        this.recvPerson = recvPerson;
    }

    public String getQtyDisplay() {
        return this.qtyDisplay;
    }

    public void setQtyDisplay(String qtyDisplay) {
        this.qtyDisplay = qtyDisplay;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public BigDecimal getPrepareQty() {
        return prepareQty;
    }

    public void setPrepareQty(BigDecimal prepareQty) {
        this.prepareQty = prepareQty;
    }

    public Long getMpTotalId() {
        return mpTotalId;
    }

    public void setMpTotalId(Long mpTotalId) {
        this.mpTotalId = mpTotalId;
    }

    public Double getMaterialNps() {
        return materialNps;
    }

    public void setMaterialNps(Double materialNps) {
        this.materialNps = materialNps;
    }

    public Double getMaterialLength() {
        return materialLength;
    }

    public void setMaterialLength(Double materialLength) {
        this.materialLength = materialLength;
    }

    public String getMaterialNpsText() {
        return materialNpsText;
    }

    public void setMaterialNpsText(String materialNpsText) {
        this.materialNpsText = materialNpsText;
    }

    public String getMaterialLengthText() {
        return materialLengthText;
    }

    public void setMaterialLengthText(String materialLengthText) {
        this.materialLengthText = materialLengthText;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public LengthUnit getMaterialNpsUnit() {
        return materialNpsUnit;
    }

    public void setMaterialNpsUnit(LengthUnit materialNpsUnit) {
        this.materialNpsUnit = materialNpsUnit;
    }
}
