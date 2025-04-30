package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 入库材料二维码查询DTO
 */
public class MmReleaseReceiveInventoryQrCodeDTO extends BaseDTO {

    private static final long serialVersionUID = 743944653360350709L;

    @Schema(description = "二维码")
    public String qrCode;

    @Schema(description = "件数")
    public Integer pieceQty;

    @Schema(description = "内检合格数量")
    public Integer qualifiedPieceQty;

    @Schema(description = "外检合格数量")
    public Integer externalQualifiedPieceQty;

    @Schema(description = "总量")
    public Double totalQty;

    @Schema(description = "内检合格总量")
    public Double qualifiedTotalQty;

    @Schema(description = "外检合格总量")
    public Double externalQualifiedTotalQty;

    @Schema(description = "仓库ID")
    public Long wareHouseLocationId;

    @Schema(description = "是否在外检中")
    public Boolean inExternalQuality;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Integer getPieceQty() {
        return pieceQty;
    }

    public void setPieceQty(Integer pieceQty) {
        this.pieceQty = pieceQty;
    }

    public Integer getQualifiedPieceQty() {
        return qualifiedPieceQty;
    }

    public void setQualifiedPieceQty(Integer qualifiedPieceQty) {
        this.qualifiedPieceQty = qualifiedPieceQty;
    }

    public Double getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Double totalQty) {
        this.totalQty = totalQty;
    }

    public Double getQualifiedTotalQty() {
        return qualifiedTotalQty;
    }

    public void setQualifiedTotalQty(Double qualifiedTotalQty) {
        this.qualifiedTotalQty = qualifiedTotalQty;
    }

    public Long getWareHouseLocationId() {
        return wareHouseLocationId;
    }

    public void setWareHouseLocationId(Long wareHouseLocationId) {
        this.wareHouseLocationId = wareHouseLocationId;
    }

    public Integer getExternalQualifiedPieceQty() {
        return externalQualifiedPieceQty;
    }

    public void setExternalQualifiedPieceQty(Integer externalQualifiedPieceQty) {
        this.externalQualifiedPieceQty = externalQualifiedPieceQty;
    }

    public Double getExternalQualifiedTotalQty() {
        return externalQualifiedTotalQty;
    }

    public void setExternalQualifiedTotalQty(Double externalQualifiedTotalQty) {
        this.externalQualifiedTotalQty = externalQualifiedTotalQty;
    }

    public Boolean getInExternalQuality() {
        return inExternalQuality;
    }

    public void setInExternalQuality(Boolean inExternalQuality) {
        this.inExternalQuality = inExternalQuality;
    }
}
