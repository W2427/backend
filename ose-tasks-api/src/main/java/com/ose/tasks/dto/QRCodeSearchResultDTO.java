package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import com.ose.vo.QrcodePrefixType;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 二维码创建表单。
 */
public class QRCodeSearchResultDTO extends BaseDTO {

    private static final long serialVersionUID = 3306151730639645725L;

    @Schema(description = "类型")
    private QrcodePrefixType type;

    @Schema(description = "查询结果")
    private BaseDTO result;

    public QrcodePrefixType getType() {
        return type;
    }

    public void setType(QrcodePrefixType type) {
        this.type = type;
    }

    public BaseDTO getResult() {
        return result;
    }

    public void setResult(BaseDTO result) {
        this.result = result;
    }

}
