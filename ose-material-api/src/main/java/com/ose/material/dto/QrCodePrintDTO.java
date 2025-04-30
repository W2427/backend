package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 二维码创建表单。
 */
public class QrCodePrintDTO extends BaseDTO {

    private static final long serialVersionUID = -9201888637523716810L;
    @Schema(description = "QRCode id列表")
    private List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }


}
