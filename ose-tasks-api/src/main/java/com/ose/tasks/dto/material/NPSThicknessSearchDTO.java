package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 数据传输对象。
 */
public class NPSThicknessSearchDTO extends BaseDTO {

    public NPSThicknessSearchDTO() {
        super();
    }

    public NPSThicknessSearchDTO(String nps, String thicknessSch) {
        super();
        this.nps = nps;
        this.thicknessSch = thicknessSch;
    }

    public NPSThicknessSearchDTO(String nps, String npsValue, String thicknessSch) {
        super();
        this.nps = nps;
        this.npsValue = npsValue;
        this.thicknessSch = thicknessSch;
    }

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "nps")
    private String nps;

    @Schema(description = "npsValue")
    private String npsValue;

    @Schema(description = "壁厚等级")
    private String thicknessSch;

    public String getNps() {
        return nps;
    }

    public void setNps(String nps) {
        this.nps = nps;
    }

    public String getThicknessSch() {
        return thicknessSch;
    }

    public void setThicknessSch(String thicknessSch) {
        this.thicknessSch = thicknessSch;
    }

    public String getNpsValue() {
        return npsValue;
    }

    public void setNpsValue(String npsValue) {
        this.npsValue = npsValue;
    }

}
