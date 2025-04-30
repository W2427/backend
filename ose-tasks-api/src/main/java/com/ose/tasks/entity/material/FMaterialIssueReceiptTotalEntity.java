package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * 出库报告合计实体类。
 */
@Entity
@Table(name = "mat_f_material_issue_receipt_total")
public class FMaterialIssueReceiptTotalEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "project_id")
    private Long projectId;

    // 出库详情单号
    @Column(name = "fmird_code")
    private String fmirdCode;

    // 出库单号ID
    @Column(name = "fmir_id")
    private Long fmirId;

    @Schema(description = "领料单详情ID")
    @Column(name = "fmreq_item_id")
    private Long fmreqItemId;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "ident码")
    private String ident;

    // 规格
    @Column(name = "spec")
    private String spec;

    @Schema(description = "材质")
    private String texture;

    @Schema(description = "计量单位")
    private String unitCode;

    @Schema(description = "领料数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal resvQty = BigDecimal.ZERO;

    @Schema(description = "出库数量。退库的数量为负")
    @Column(precision = 19, scale = 3)
    private BigDecimal issueQty = BigDecimal.ZERO;

    @Schema(description = "超额发出/出库的量")
    @Column(precision = 19, scale = 3)
    private BigDecimal overIssueQty = BigDecimal.ZERO;

    @Schema(description = "合计出库件数")
    private int totalIssueCnt = 0;

    @Schema(description = "实际配送数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal actualTransferQty = BigDecimal.ZERO;

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

    public String getFmirdCode() {
        return fmirdCode;
    }

    public void setFmirdCode(String fmirdCode) {
        this.fmirdCode = fmirdCode;
    }

    public Long getFmirId() {
        return fmirId;
    }

    public void setFmirId(Long fmirId) {
        this.fmirId = fmirId;
    }

    public BigDecimal getResvQty() {
        return resvQty;
    }

    public void setResvQty(BigDecimal resvQty) {
        this.resvQty = resvQty;
    }

    public Long getFmreqItemId() {
        return fmreqItemId;
    }

    public void setFmreqItemId(Long fmreqItemId) {
        this.fmreqItemId = fmreqItemId;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public BigDecimal getIssueQty() {
        return issueQty;
    }

    public void setIssueQty(BigDecimal issueQty) {
        this.issueQty = issueQty;
    }

    public BigDecimal getOverIssueQty() {
        return overIssueQty;
    }

    public void setOverIssueQty(BigDecimal overIssueQty) {
        this.overIssueQty = overIssueQty;
    }

    public int getTotalIssueCnt() {
        return totalIssueCnt;
    }

    public void setTotalIssueCnt(int totalIssueCnt) {
        this.totalIssueCnt = totalIssueCnt;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public BigDecimal getActualTransferQty() {
        return actualTransferQty;
    }

    public void setActualTransferQty(BigDecimal actualTransferQty) {
        this.actualTransferQty = actualTransferQty;
    }

    @Schema(description = "领料单详情信息")
    @JsonProperty(value = "fmreqItemId")
    public ReferenceData getFmreqItemIdRef() {
        return this.fmreqItemId == null
            ? null
            : new ReferenceData(this.fmreqItemId);
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }
}
