package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseEntity;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 材料盘点OSD实体类。
 */
@Entity
@Table(name = "mat_f_material_stocktake_osd")
public class FMaterialStocktakeOSDEntity extends BaseEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "fmst_id")
    private Long fmstId;

    @Column(name = "reln_id")
    private Long relnId;

    @Column(name = "reln_item_id")
    private Long relnItemId;

    private String ident;

    @Schema(description = "放行量")
    @Column(precision = 19, scale = 3)
    private BigDecimal relnQty = BigDecimal.ZERO;

    @Schema(description = "超出量")
    @Column(name = "over_qty", precision = 19, scale = 3)
    private BigDecimal overQty;

    @Schema(description = "缺少量")
    @Column(name = "shortage_qty", precision = 19, scale = 3)
    private BigDecimal shortageQty;

    @Schema(description = "损坏量")
    @Column(name = "damage_qty", precision = 19, scale = 3)
    private BigDecimal damageQty;

    @Schema(description = "创建时间")
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date createdAt;

    @Schema(description = "最后更新时间")
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date lastModifiedAt;

    @Schema(description = "数据实体状态")
    @Column(length = 16)
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

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

    public BigDecimal getRelnQty() {
        return relnQty;
    }

    public void setRelnQty(BigDecimal relnQty) {
        this.relnQty = relnQty;
    }

    public BigDecimal getOverQty() {
        return overQty;
    }

    public void setOverQty(BigDecimal overQty) {
        this.overQty = overQty;
    }

    public BigDecimal getShortageQty() {
        return shortageQty;
    }

    public void setShortageQty(BigDecimal shortageQty) {
        this.shortageQty = shortageQty;
    }

    public BigDecimal getDamageQty() {
        return damageQty;
    }

    public void setDamageQty(BigDecimal damageQty) {
        this.damageQty = damageQty;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Schema(description = "放行单详情信息")
    @JsonProperty(value = "relnItemId", access = READ_ONLY)
    public ReferenceData getRelnItemIdRef() {
        return this.relnItemId == null
            ? null
            : new ReferenceData(this.relnItemId);
    }
}
