package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class FMaterialLockedPostDTO extends BaseDTO {

    private static final long serialVersionUID = -6454646531908379274L;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "材料NPS")
    private Double nps;

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public Double getNps() {
        return nps;
    }

    public void setNps(Double nps) {
        this.nps = nps;
    }
}
