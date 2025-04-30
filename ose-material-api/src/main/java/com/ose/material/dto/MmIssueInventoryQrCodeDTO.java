package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 出库材料二维码查询DTO
 */
public class MmIssueInventoryQrCodeDTO extends BaseDTO {

    private static final long serialVersionUID = -8295693184811987688L;

    @Schema(description = "二维码")
    public String qrCode;

    @Schema(description = "件号")
    public String pieceTagNo;

    @Schema(description = "总量")
    public Double totalQty;

    @Schema(description = "件数")
    public Integer pieceQty;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Double getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Double totalQty) {
        this.totalQty = totalQty;
    }

    public Integer getPieceQty() {
        return pieceQty;
    }

    public void setPieceQty(Integer pieceQty) {
        this.pieceQty = pieceQty;
    }

    public String getPieceTagNo() {
        return pieceTagNo;
    }

    public void setPieceTagNo(String pieceTagNo) {
        this.pieceTagNo = pieceTagNo;
    }
}
