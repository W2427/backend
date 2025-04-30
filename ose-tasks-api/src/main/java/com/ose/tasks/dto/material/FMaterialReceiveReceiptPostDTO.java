package com.ose.tasks.dto.material;

import java.util.Map;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 入库清单DTO
 */
public class FMaterialReceiveReceiptPostDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;


    @Schema(description = "入库单")
    private String fmrrCode;

    @Schema(description = "qrcode 和 数量")
    private Map<String, Integer> qrCodeMap;

    public String getFmrrCode() {
        return fmrrCode;
    }

    public void setFmrrCode(String fmrrCode) {
        this.fmrrCode = fmrrCode;
    }

    public Map<String, Integer> getQrCodeMap() {
        return qrCodeMap;
    }

    public void setQrCodeMap(Map<String, Integer> qrCodeMap) {
        this.qrCodeMap = qrCodeMap;
    }

}
