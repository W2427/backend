package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 数据传输对象。
 */
public class NPSThicknessDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "壁厚值")
    private double thickness;

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

}
