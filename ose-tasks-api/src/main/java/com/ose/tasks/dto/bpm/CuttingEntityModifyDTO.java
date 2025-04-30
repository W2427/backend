package com.ose.tasks.dto.bpm;


import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class CuttingEntityModifyDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 1663208846470310343L;

    @Schema(description = "实体二维码Code")
    private String qrCode;

    @Schema(description = "炉批号")
    private String heatNoCode;

    @Schema(description = "描述")
    private String shortDesc;

    @Schema(description = "切割状态")
    private CuttingFlag cuttingflag;

    @Schema(description = "备注")
    private String memo;


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public CuttingFlag getCuttingflag() {
        return cuttingflag;
    }

    public void setCuttingflag(CuttingFlag cuttingflag) {
        this.cuttingflag = cuttingflag;
    }

    public enum CuttingFlag {
        UNDO,
        DONE
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

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

}
