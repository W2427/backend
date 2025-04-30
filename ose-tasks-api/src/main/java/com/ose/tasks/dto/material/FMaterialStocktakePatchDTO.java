package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 更新盘点清单DTO
 */
public class FMaterialStocktakePatchDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "描述")
    private String desc;

    @Schema(description = "盘点结果")
    private String stocktakeResult;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStocktakeResult() {
        return stocktakeResult;
    }

    public void setStocktakeResult(String stocktakeResult) {
        this.stocktakeResult = stocktakeResult;
    }

}
