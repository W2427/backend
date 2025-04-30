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
 * 入库报告合计实体类。
 */
@Entity
@Table(name = "mat_f_material_receive_receipt_total")
public class FMaterialReceiveReceiptTotalEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "project_id")
    private Long projectId;

    // 入库详情单号
    @Column(name = "fmrrd_code")
    private String fmrrdCode;

    // 入单号ID
    @Column(name = "fmrr_id")
    private Long fmrrId;

    @Column(name = "reln_id")
    private Long relnId;

    @Column(name = "reln_item_id")
    private Long relnItemId;

    @Column(name = "receive_qty", precision = 19, scale = 3)
    private BigDecimal receiveQty;

    @Schema(description = "IDENT码")
    private String ident;

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

    public String getFmrrdCode() {
        return fmrrdCode;
    }

    public void setFmrrdCode(String fmrrdCode) {
        this.fmrrdCode = fmrrdCode;
    }

    public Long getFmrrId() {
        return fmrrId;
    }

    public void setFmrrId(Long fmrrId) {
        this.fmrrId = fmrrId;
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

    public BigDecimal getReceiveQty() {
        return receiveQty;
    }

    public void setReceiveQty(BigDecimal receiveQty) {
        this.receiveQty = receiveQty;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    @Schema(description = "放行单详情信息")
    @JsonProperty(value = "relnItemId", access = READ_ONLY)
    public ReferenceData getRelnItemIdRef() {
        return this.relnItemId == null
            ? null
            : new ReferenceData(this.relnItemId);
    }
}
