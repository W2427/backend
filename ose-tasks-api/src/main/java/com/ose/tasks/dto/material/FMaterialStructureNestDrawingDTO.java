package com.ose.tasks.dto.material;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

public class FMaterialStructureNestDrawingDTO extends BaseVersionedBizEntity {

    @Schema(description = "WP1编号")
    private String wp01No;

    @Schema(description = "WP2编号")
    private String wp02No;

    @Schema(description = "WP3编号")
    private String wp03No;

    @Schema(description = "WP4编号")
    private String wp04No;

    @Schema(description = "WP5编号")
    private String wp05No;

    @Schema(description = "数量")
    private String qty;

    @Schema(description = "托盘号")
    private String trayNo;

    @Schema(description = "排版名")
    private String nestingProgramNo;

    @Schema(description = "材料二维码")
    private String materialQrCode;

    @Schema(description = "材料炉批号")
    private String materialHeatNo;

    public String getWp01No() {
        return wp01No;
    }

    public void setWp01No(String wp01No) {
        this.wp01No = wp01No;
    }

    public String getWp02No() {
        return wp02No;
    }

    public void setWp02No(String wp02No) {
        this.wp02No = wp02No;
    }

    public String getWp03No() {
        return wp03No;
    }

    public void setWp03No(String wp03No) {
        this.wp03No = wp03No;
    }

    public String getWp04No() {
        return wp04No;
    }

    public void setWp04No(String wp04No) {
        this.wp04No = wp04No;
    }

    public String getWp05No() {
        return wp05No;
    }

    public void setWp05No(String wp05No) {
        this.wp05No = wp05No;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTrayNo() {
        return trayNo;
    }

    public void setTrayNo(String trayNo) {
        this.trayNo = trayNo;
    }

    public String getNestingProgramNo() {
        return nestingProgramNo;
    }

    public void setNestingProgramNo(String nestingProgramNo) {
        this.nestingProgramNo = nestingProgramNo;
    }

    public String getMaterialQrCode() {
        return materialQrCode;
    }

    public void setMaterialQrCode(String materialQrCode) {
        this.materialQrCode = materialQrCode;
    }

    public String getMaterialHeatNo() {
        return materialHeatNo;
    }

    public void setMaterialHeatNo(String materialHeatNo) {
        this.materialHeatNo = materialHeatNo;
    }
}
