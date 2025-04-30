package com.ose.tasks.entity.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Spm M_LIST_POS 缓存表
 */

@Entity
@Table(name = "spm_list_pos",
    indexes = {
        @Index(columnList = "ln_id"),
        @Index(columnList = "tag_number"),
        @Index(columnList = "proj_id")
    })
public class SpmListPos extends BaseDTO {

    /**
     * LN_ID
     * LP_ID
     * DP_ID
     * PROJ_ID
     * LP_POS
     * QUANTITY
     * LMOD
     * COMMODITY_ID
     * IDENT
     * RESV_QTY
     * TAG_NUMBER
     * ISSUE_QTY
     */


    @Schema(description = "SPM LP ID")
    @Id
    private BigDecimal lp_id;

    @Schema(description = "SPM LN_ID")
    private BigDecimal ln_id;

    @Schema(description = "SPM DP ID discipline")
    private BigDecimal dp_id;

    @Schema(description = "spm project id")
    private String proj_id;

    @Schema(description = "SPM LP_POS 排序位置")
    private String lp_pos;

    @Schema(description = "SPM total quantity")
    private BigDecimal quantity;

    @Schema(description = "SPM Last modified")
    private Date lmod;

    @Schema(description = "SPM COMMODITY_ID")
    private BigDecimal commodity_id;

    @Schema(description = "SPM IDENT")
    private BigDecimal ident;

    @Schema(description = "SPM resv qty")
    private BigDecimal resv_qty;

    @Schema(description = "SPM TAG_NUMBER")
    private String tag_number;

    @Schema(description = "SPM ISSUE_QTY")
    private BigDecimal issue_qty;

    @Schema(description = "材料描述")
    private String short_desc;

    public BigDecimal getLn_id() {
        return ln_id;
    }

    public void setLn_id(BigDecimal ln_id) {
        this.ln_id = ln_id;
    }

    public BigDecimal getLp_id() {
        return lp_id;
    }

    public void setLp_id(BigDecimal lp_id) {
        this.lp_id = lp_id;
    }

    public BigDecimal getDp_id() {
        return dp_id;
    }

    public void setDp_id(BigDecimal dp_id) {
        this.dp_id = dp_id;
    }

    public String getProj_id() {
        return proj_id;
    }

    public void setProj_id(String proj_id) {
        this.proj_id = proj_id;
    }

    public String getLp_pos() {
        return lp_pos;
    }

    public void setLp_pos(String lp_pos) {
        this.lp_pos = lp_pos;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Date getLmod() {
        return lmod;
    }

    public void setLmod(Date lmod) {
        this.lmod = lmod;
    }

    public BigDecimal getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(BigDecimal commodity_id) {
        this.commodity_id = commodity_id;
    }

    public BigDecimal getIdent() {
        return ident;
    }

    public void setIdent(BigDecimal ident) {
        this.ident = ident;
    }

    public BigDecimal getResv_qty() {
        return resv_qty;
    }

    public void setResv_qty(BigDecimal resv_qty) {
        this.resv_qty = resv_qty;
    }

    public String getTag_number() {
        return tag_number;
    }

    public void setTag_number(String tag_number) {
        this.tag_number = tag_number;
    }

    public BigDecimal getIssue_qty() {
        return issue_qty;
    }

    public void setIssue_qty(BigDecimal issue_qty) {
        this.issue_qty = issue_qty;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }
}
