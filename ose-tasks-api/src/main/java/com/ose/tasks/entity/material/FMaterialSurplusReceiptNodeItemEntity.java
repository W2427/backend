package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 余料领料单实体类。
 */
@Entity
@Table(name = "mat_f_material_surplus_receipt_node_item")
public class FMaterialSurplusReceiptNodeItemEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -1546120881723492886L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "描述")
    private String memo;

    @Schema(description = "余料领料单号")
    private String fMaterialSurplusReceiptNo;

    @Schema(description = "余料领料单ID")
    private Long fMaterialSurplusReceiptId;

    @Schema(description = "实体id")
    private Long entityId;

    @Schema(description = "余料领料单节点id")
    private Long fMaterialSurplusReceiptNodeEntityId;

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

    public String getfMaterialSurplusReceiptNo() {
        return fMaterialSurplusReceiptNo;
    }

    public void setfMaterialSurplusReceiptNo(String fMaterialSurplusReceiptNo) {
        this.fMaterialSurplusReceiptNo = fMaterialSurplusReceiptNo;
    }

    public Long getfMaterialSurplusReceiptId() {
        return fMaterialSurplusReceiptId;
    }

    public void setfMaterialSurplusReceiptId(Long fMaterialSurplusReceiptId) {
        this.fMaterialSurplusReceiptId = fMaterialSurplusReceiptId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getfMaterialSurplusReceiptNodeEntityId() {
        return fMaterialSurplusReceiptNodeEntityId;
    }

    public void setfMaterialSurplusReceiptNodeEntityId(Long fMaterialSurplusReceiptNodeEntityId) {
        this.fMaterialSurplusReceiptNodeEntityId = fMaterialSurplusReceiptNodeEntityId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
