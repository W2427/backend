package com.ose.tasks.dto.material;

import com.ose.dto.PageDTO;
import com.ose.tasks.vo.material.FItemStatusType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 二维码补码查询dto
 */
public class FItemQrCodeCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = 2974149128486400958L;

    @Schema(description = "ident码")
    private String ident;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "描述")
    private String shortDesc;

    @Schema(description = "材料规格")
    private String spec;

    @Schema(description = "炉批号")
    private String heatNo;

    @Schema(description = "二维码")
    private String qrCode;

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "在库状态")
    private FItemStatusType itemStatus;

    public FItemStatusType getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(FItemStatusType itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}
