package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 二维码生产DTO
 */
public class ReleaseNoteItemDetailQrCodeDto extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "二维码数量")
    private int qrCodeQty;

    public int getQrCodeQty() {
        return qrCodeQty;
    }

    public void setQrCodeQty(int qrCodeQty) {
        this.qrCodeQty = qrCodeQty;
    }

}
