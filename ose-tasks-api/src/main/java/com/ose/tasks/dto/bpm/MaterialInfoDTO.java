package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 临时文件信息。
 */
public class MaterialInfoDTO extends BaseDTO {

    private static final long serialVersionUID = -4059288973826028452L;

    @Schema(description = "实体二维码Code")
    private String qrCode;

    @Schema(description = "炉批号")
    private String heatNoCode;

    @Schema(description = "描述")
    private String shortDesc;

    public String getHeatNoCode() {
        return heatNoCode;
    }

    public void setHeatNoCode(String heatNoCode) {
        this.heatNoCode = heatNoCode;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

}
