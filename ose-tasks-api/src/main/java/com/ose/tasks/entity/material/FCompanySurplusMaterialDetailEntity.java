package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 施工单位余料详情表实体
 */
@Entity
@Table(name = "mat_f_company_surplus_material_detail")
public class FCompanySurplusMaterialDetailEntity extends BaseBizEntity {

    private static final long serialVersionUID = -2666528271379568366L;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "company_code")
    private String companyCode;

    @Column(name = "parent_id")
    private Long parentId;

    // 单位量
    @Column(name = "qty", precision = 19, scale = 3)
    private BigDecimal qty;

    // 数量
    @Column(name = "qty_cnt")
    private int qtyCnt;

    @Column(precision = 19, scale = 3)
    private BigDecimal itemTotalQty;

    @Schema(description = "关联的套料材料明细ID")
    private Long relationshipNestMaterialItemId;

    @Schema(description = "关联的材料准备单ID")
    private Long relationshipMaterialPrepareId;

    @Column(name = "issue_relationship_id")
    private Long issueRelationshipId;

    @Column(name = "return_relationship_id")
    private Long returnRelationshipId;

    @Column(name = "rn_qr_code_id")
    private Long rnQrCodeId;

    @Column(name = "rn_qr_code")
    private String rnQrCode;

    @Column(name = "locked")
    @Schema(description = "材料是否被套料锁定，如果锁定，则不会显示到套料查询中")
    private Boolean locked = false;

    @Column(name = "locked_id")
    private Long lockedId;

    //    // issue, return
//    @Column(name="item_status")
//    @Enumerated(EnumType.STRING)
//    private FItemStatusType itemStatus;
//
    @Schema(description = "材料批次号")
    private String materialBatchNumber;

    @Schema(description = "接收状态，true：接收，false：未接收")
    private boolean receivedFlg = false;

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Long getLockedId() {
        return lockedId;
    }

    public void setLockedId(Long lockedId) {
        this.lockedId = lockedId;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public BigDecimal getItemTotalQty() {
        return itemTotalQty;
    }

    public void setItemTotalQty(BigDecimal itemTotalQty) {
        this.itemTotalQty = itemTotalQty;
    }

    public Long getIssueRelationshipId() {
        return issueRelationshipId;
    }

    public void setIssueRelationshipId(Long issueRelationshipId) {
        this.issueRelationshipId = issueRelationshipId;
    }

    public Long getReturnRelationshipId() {
        return returnRelationshipId;
    }

    public void setReturnRelationshipId(Long returnRelationshipId) {
        this.returnRelationshipId = returnRelationshipId;
    }

    public Long getRnQrCodeId() {
        return rnQrCodeId;
    }

    public void setRnQrCodeId(Long rnQrCodeId) {
        this.rnQrCodeId = rnQrCodeId;
    }

    public String getRnQrCode() {
        return rnQrCode;
    }

    public void setRnQrCode(String rnQrCode) {
        this.rnQrCode = rnQrCode;
    }

    //
//    public FItemStatusType getItemStatus() {
//        return itemStatus;
//    }
//
//    public void setItemStatus(FItemStatusType itemStatus) {
//        this.itemStatus = itemStatus;
//    }
//
    public String getMaterialBatchNumber() {
        return materialBatchNumber;
    }

    public void setMaterialBatchNumber(String materialBatchNumber) {
        this.materialBatchNumber = materialBatchNumber;
    }

    public boolean isReceivedFlg() {
        return receivedFlg;
    }

    public void setReceivedFlg(boolean receivedFlg) {
        this.receivedFlg = receivedFlg;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    @Schema(description = "余料主表")
    @JsonProperty(value = "parentId", access = READ_ONLY)
    public ReferenceData getParentIdRef() {
        return this.parentId == null
            ? null
            : new ReferenceData(this.parentId);
    }
}
