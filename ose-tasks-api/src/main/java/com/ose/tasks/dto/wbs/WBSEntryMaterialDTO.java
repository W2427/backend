package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Spm M_LIST_POS 映射
 */
@SqlResultSetMapping
    (
        name = "WBSEntryMaterialDTOSqlResultMapping",
        entities = {
            @EntityResult(
                entityClass = WBSEntryMaterialDTO.class, //就是当前这个类的名字
                fields = {
                    @FieldResult(name = "ident", column = "ident"),
                    @FieldResult(name = "quantity", column = "quantity"),
                    @FieldResult(name = "resvQty", column = "resv_qty"),
                    @FieldResult(name = "tagNumber", column = "tag_number"),
                    @FieldResult(name = "issueQty", column = "issue_qty"),
                    @FieldResult(name = "lackQty", column = "lack_qty"),
                    @FieldResult(name = "shortDesc", column = "short_desc")
                }
            )
        }
    )
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WBSEntryMaterialDTO {

    @Schema(description = "SPM IDENT")
    @Id
    private BigDecimal ident;

    @Schema(description = "SPM total quantity")
    private BigDecimal quantity;

    @Schema(description = "SPM resv qty")
    private BigDecimal resvQty;

    @Schema(description = "SPM TAG_NUMBER")
    private String tagNumber;

    @Schema(description = "SPM ISSUE_QTY")
    private BigDecimal issueQty;

    @Schema(description = "LACK_QTY")
    private BigDecimal lackQty;

    @Schema(description = "材料描述")
    private String shortDesc;

    public BigDecimal getIdent() {
        return ident;
    }

    public void setIdent(BigDecimal ident) {
        this.ident = ident;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getResvQty() {
        return resvQty;
    }

    public void setResvQty(BigDecimal resvQty) {
        this.resvQty = resvQty;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public BigDecimal getIssueQty() {
        return issueQty;
    }

    public void setIssueQty(BigDecimal issueQty) {
        this.issueQty = issueQty;
    }

    public BigDecimal getLackQty() {
        return lackQty;
    }

    public void setLackQty(BigDecimal lackQty) {
        this.lackQty = lackQty;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }
}
