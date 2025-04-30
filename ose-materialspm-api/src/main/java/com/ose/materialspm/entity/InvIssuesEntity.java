package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 库存出库表
 */
@Entity
@Table(name = "m_inv_issues")
@NamedQuery(name = "InvIssuesEntity.findAll", query = "SELECT a FROM InvIssuesEntity a")
public class InvIssuesEntity extends BaseDTO {

    private static final long serialVersionUID = -2911907681217718039L;
    @Id
    @Column(name = "iis_id", nullable = false)
    private Long iisId;

    @Column(name = "proj_id", nullable = false, length = 10)
    private String projId;

    @Column(name = "ivi_id", nullable = false)
    private Long iviId;

    @Column(name = "mir_id", nullable = false)
    private Long mirId;

    @Column(name = "dp_id", nullable = false)
    private Long dpId;

    @Column(name = "lp_id")
    private Long lpId;

    @Column(name = "issue_qty", nullable = false)
    private Double issueQty;

    @Column(name = "issue_date", nullable = false)
    private Date issueDate;

    @Column(name = "usr_id", nullable = false, length = 20)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "overissue_qty", nullable = false)
    private Double overissueQty;

    @Column(name = "upd_rev_ind", nullable = false, length = 1)
    private String updRevInd;

    @Column(name = "rev_overissue_qty", nullable = false)
    private Double revOverissueQty;

    @Column(name = "resv_qty", nullable = false)
    private Double resvQty;

    @Column(name = "use_resv_ind", nullable = false, length = 1)
    private String useResvInd;

    @Column(name = "mld_qty", nullable = false)
    private Double mldQty;

    @Column(name = "mld_reg_type", nullable = false, length = 2)
    private String mldRegType;

    @Column(name = "used_oi_qty", nullable = false)
    private Double usedOiQty;

    @Column(name = "transfer_scrap_qty")
    private Double transferScrapQty;

    @Column(name = "parent_iis_id")
    private Long parentIisId;

    @Column(name = "reference_iis_id")
    private Long referenceIisId;

    @Column(name = "last_iis_id")
    private Long lastIisId;

    @Column(name = "issued_lp_id")
    private Long issuedLpId;

    @Column(name = "ivpr_id")
    private Long ivprId;

    @Column(name = "over_issue_ivi_id")
    private Long overIssueIviId;

    @Column(name = "subst_ind", nullable = false, length = 1)
    private String substInd;

    @Column(name = "trans_ind", nullable = false, length = 1)
    private String transInd;

    @Column(name = "transform_ind", nullable = false, length = 1)
    private String transformInd;

    @Column(name = "sel_ind", nullable = false, length = 1)
    private String selInd;

    @Column(name = "subst_pk_id")
    private Long substPkId;

    @Column(name = "spmat_error_text", length = 255)
    private String spmatErrorText;

    @Column(name = "attr1_id")
    private Long attr1Id;

    @Column(name = "attr2_id")
    private Long attr2Id;

    @Column(name = "attr3_id")
    private Long attr3Id;

    @Column(name = "attr1_value", length = 255)
    private String attr1Value;

    @Column(name = "attr2_value", length = 255)
    private String attr2Value;

    @Column(name = "attr3_value", length = 255)
    private String attr3Value;

    @Column(name = "rti_ind", nullable = false, length = 1)
    private String rtiInd;

    @Column(name = "item_ship_id")
    private Long itemShipId;

    @Column(name = "dci_qty", nullable = false)
    private Double dciQty;

    @Column(name = "dci_ivi_id")
    private Long dciIviId;

    @Column(name = "rev_dci_ind", nullable = false, length = 1)
    private String revDciInd;

    @Column(name = "mpt_id")
    private Long mptId;

    public Long getIisId() {
        return iisId;
    }

    public void setIisId(Long iisId) {
        this.iisId = iisId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Long getIviId() {
        return iviId;
    }

    public void setIviId(Long iviId) {
        this.iviId = iviId;
    }

    public Long getMirId() {
        return mirId;
    }

    public void setMirId(Long mirId) {
        this.mirId = mirId;
    }

    public Long getDpId() {
        return dpId;
    }

    public void setDpId(Long dpId) {
        this.dpId = dpId;
    }

    public Long getLpId() {
        return lpId;
    }

    public void setLpId(Long lpId) {
        this.lpId = lpId;
    }

    public Double getIssueQty() {
        return issueQty;
    }

    public void setIssueQty(Double issueQty) {
        this.issueQty = issueQty;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public Date getLmod() {
        return lmod;
    }

    public void setLmod(Date lmod) {
        this.lmod = lmod;
    }

    public Integer getIntRev() {
        return intRev;
    }

    public void setIntRev(Integer intRev) {
        this.intRev = intRev;
    }

    public Double getOverissueQty() {
        return overissueQty;
    }

    public void setOverissueQty(Double overissueQty) {
        this.overissueQty = overissueQty;
    }

    public String getUpdRevInd() {
        return updRevInd;
    }

    public void setUpdRevInd(String updRevInd) {
        this.updRevInd = updRevInd;
    }

    public Double getRevOverissueQty() {
        return revOverissueQty;
    }

    public void setRevOverissueQty(Double revOverissueQty) {
        this.revOverissueQty = revOverissueQty;
    }

    public Double getResvQty() {
        return resvQty;
    }

    public void setResvQty(Double resvQty) {
        this.resvQty = resvQty;
    }

    public String getUseResvInd() {
        return useResvInd;
    }

    public void setUseResvInd(String useResvInd) {
        this.useResvInd = useResvInd;
    }

    public Double getMldQty() {
        return mldQty;
    }

    public void setMldQty(Double mldQty) {
        this.mldQty = mldQty;
    }

    public String getMldRegType() {
        return mldRegType;
    }

    public void setMldRegType(String mldRegType) {
        this.mldRegType = mldRegType;
    }

    public Double getUsedOiQty() {
        return usedOiQty;
    }

    public void setUsedOiQty(Double usedOiQty) {
        this.usedOiQty = usedOiQty;
    }

    public Double getTransferScrapQty() {
        return transferScrapQty;
    }

    public void setTransferScrapQty(Double transferScrapQty) {
        this.transferScrapQty = transferScrapQty;
    }

    public Long getParentIisId() {
        return parentIisId;
    }

    public void setParentIisId(Long parentIisId) {
        this.parentIisId = parentIisId;
    }

    public Long getReferenceIisId() {
        return referenceIisId;
    }

    public void setReferenceIisId(Long referenceIisId) {
        this.referenceIisId = referenceIisId;
    }

    public Long getLastIisId() {
        return lastIisId;
    }

    public void setLastIisId(Long lastIisId) {
        this.lastIisId = lastIisId;
    }

    public Long getIssuedLpId() {
        return issuedLpId;
    }

    public void setIssuedLpId(Long issuedLpId) {
        this.issuedLpId = issuedLpId;
    }

    public Long getIvprId() {
        return ivprId;
    }

    public void setIvprId(Long ivprId) {
        this.ivprId = ivprId;
    }

    public Long getOverIssueIviId() {
        return overIssueIviId;
    }

    public void setOverIssueIviId(Long overIssueIviId) {
        this.overIssueIviId = overIssueIviId;
    }

    public String getSubstInd() {
        return substInd;
    }

    public void setSubstInd(String substInd) {
        this.substInd = substInd;
    }

    public String getTransInd() {
        return transInd;
    }

    public void setTransInd(String transInd) {
        this.transInd = transInd;
    }

    public String getTransformInd() {
        return transformInd;
    }

    public void setTransformInd(String transformInd) {
        this.transformInd = transformInd;
    }

    public String getSelInd() {
        return selInd;
    }

    public void setSelInd(String selInd) {
        this.selInd = selInd;
    }

    public Long getSubstPkId() {
        return substPkId;
    }

    public void setSubstPkId(Long substPkId) {
        this.substPkId = substPkId;
    }

    public String getSpmatErrorText() {
        return spmatErrorText;
    }

    public void setSpmatErrorText(String spmatErrorText) {
        this.spmatErrorText = spmatErrorText;
    }

    public Long getAttr1Id() {
        return attr1Id;
    }

    public void setAttr1Id(Long attr1Id) {
        this.attr1Id = attr1Id;
    }

    public Long getAttr2Id() {
        return attr2Id;
    }

    public void setAttr2Id(Long attr2Id) {
        this.attr2Id = attr2Id;
    }

    public Long getAttr3Id() {
        return attr3Id;
    }

    public void setAttr3Id(Long attr3Id) {
        this.attr3Id = attr3Id;
    }

    public String getAttr1Value() {
        return attr1Value;
    }

    public void setAttr1Value(String attr1Value) {
        this.attr1Value = attr1Value;
    }

    public String getAttr2Value() {
        return attr2Value;
    }

    public void setAttr2Value(String attr2Value) {
        this.attr2Value = attr2Value;
    }

    public String getAttr3Value() {
        return attr3Value;
    }

    public void setAttr3Value(String attr3Value) {
        this.attr3Value = attr3Value;
    }

    public String getRtiInd() {
        return rtiInd;
    }

    public void setRtiInd(String rtiInd) {
        this.rtiInd = rtiInd;
    }

    public Long getItemShipId() {
        return itemShipId;
    }

    public void setItemShipId(Long itemShipId) {
        this.itemShipId = itemShipId;
    }

    public Double getDciQty() {
        return dciQty;
    }

    public void setDciQty(Double dciQty) {
        this.dciQty = dciQty;
    }

    public Long getDciIviId() {
        return dciIviId;
    }

    public void setDciIviId(Long dciIviId) {
        this.dciIviId = dciIviId;
    }

    public String getRevDciInd() {
        return revDciInd;
    }

    public void setRevDciInd(String revDciInd) {
        this.revDciInd = revDciInd;
    }

    public Long getMptId() {
        return mptId;
    }

    public void setMptId(Long mptId) {
        this.mptId = mptId;
    }
}
