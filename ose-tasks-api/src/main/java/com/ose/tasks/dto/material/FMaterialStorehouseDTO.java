package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class FMaterialStorehouseDTO extends BaseDTO {
    private static final long serialVersionUID = -4389401169620380670L;

    @Schema(description = "SPM项目ID")
    private String projId;

    @Schema(description = "库位")
    private String whCode;

    @Schema(description = "库位ID")
    private Integer whId;

    @Schema(description = "货位")
    private String locCode;

    @Schema(description = "货位ID")
    private Integer locId;

    @Schema(description = "货架层号")
    private String goodsShelf;

    @Schema(description = "备注")
    private String memo;

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getWhId() {
        return whId;
    }

    public void setWhId(Integer whId) {
        this.whId = whId;
    }

    public Integer getLocId() {
        return locId;
    }

    public void setLocId(Integer locId) {
        this.locId = locId;
    }
}
