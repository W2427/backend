package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class FMaterialNestParameterPostDTO extends BaseDTO {
    private static final long serialVersionUID = 6807132858127597976L;

    @Schema(description = "切割损耗量")
    private Double wastage;

    @Schema(description = "管线末端放弃量")
    private Double abandonment;

    public Double getWastage() {
        return wastage;
    }

    public void setWastage(Double wastage) {
        this.wastage = wastage;
    }

    public Double getAbandonment() {
        return abandonment;
    }

    public void setAbandonment(Double abandonment) {
        this.abandonment = abandonment;
    }
}
