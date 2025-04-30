package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 创建盘点明细DTO
 */
public class FMaterialStocktakeItemPostDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "二维码")
    private String qrCode;

    @Schema(description = "数量")
    private int cnt;

    @Schema(description = "仓库ID")
    private String whId;

    @Schema(description = "仓库")
    private String whCode;

    @Schema(description = "库位ID")
    private String locId;

    @Schema(description = "库位")
    private String locCode;

    @Schema(description = "货架-层号")
    private String goodsShelf;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getWhId() {
        return whId;
    }

    public void setWhId(String whId) {
        this.whId = whId;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getGoodsShelf() {
        return goodsShelf;
    }

    public void setGoodsShelf(String goodsShelf) {
        this.goodsShelf = goodsShelf;
    }
}
