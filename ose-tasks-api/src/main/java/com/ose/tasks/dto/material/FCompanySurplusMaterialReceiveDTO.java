package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class FCompanySurplusMaterialReceiveDTO extends BaseDTO {


    private static final long serialVersionUID = -6632339734576289203L;
    @Schema(description = "二维码")
    private String qrCode;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
