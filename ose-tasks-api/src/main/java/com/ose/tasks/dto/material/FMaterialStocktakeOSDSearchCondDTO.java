package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 更新盘点OSD DTO
 */
public class FMaterialStocktakeOSDSearchCondDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "显示超出数量")
    private boolean displayOverQtyFlg;

    @Schema(description = "显示缺失数量")
    private boolean displayShortageQtyFlg;

    @Schema(description = "显示损坏数量")
    private boolean displayDamageQtyFlg;

    public boolean isDisplayOverQtyFlg() {
        return displayOverQtyFlg;
    }

    public void setDisplayOverQtyFlg(boolean displayOverQtyFlg) {
        this.displayOverQtyFlg = displayOverQtyFlg;
    }

    public boolean isDisplayShortageQtyFlg() {
        return displayShortageQtyFlg;
    }

    public void setDisplayShortageQtyFlg(boolean displayShortageQtyFlg) {
        this.displayShortageQtyFlg = displayShortageQtyFlg;
    }

    public boolean isDisplayDamageQtyFlg() {
        return displayDamageQtyFlg;
    }

    public void setDisplayDamageQtyFlg(boolean displayDamageQtyFlg) {
        this.displayDamageQtyFlg = displayDamageQtyFlg;
    }

}
