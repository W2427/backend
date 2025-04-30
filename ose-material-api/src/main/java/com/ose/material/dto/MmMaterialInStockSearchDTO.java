package com.ose.material.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 在库材料主表查询DTO
 */
public class MmMaterialInStockSearchDTO extends PageDTO {

    private static final long serialVersionUID = -7135958533891550319L;

    @Schema(description = "搜索关键字")
    public String keyword;

    @Schema(description = "二维码")
    public String qrCode;

    @Schema(description = "件号")
    public String pieceTagNo;

    @Schema(description = "IDENT码")
    public String identCode;

    @Schema(description = "材料编码")
    public String materialCodeNo;

    @Schema(description = "材料描述")
    public String mmMaterialCodeDescription;

    @Schema(description = "规格名称")
    public String specName;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getMaterialCodeNo() {
        return materialCodeNo;
    }

    public void setMaterialCodeNo(String materialCodeNo) {
        this.materialCodeNo = materialCodeNo;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getMmMaterialCodeDescription() {
        return mmMaterialCodeDescription;
    }

    public void setMmMaterialCodeDescription(String mmMaterialCodeDescription) {
        this.mmMaterialCodeDescription = mmMaterialCodeDescription;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getIdentCode() {
        return identCode;
    }

    public void setIdentCode(String identCode) {
        this.identCode = identCode;
    }

    public String getPieceTagNo() {
        return pieceTagNo;
    }

    public void setPieceTagNo(String pieceTagNo) {
        this.pieceTagNo = pieceTagNo;
    }
}
