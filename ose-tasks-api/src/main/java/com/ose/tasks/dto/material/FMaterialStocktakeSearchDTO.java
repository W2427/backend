package com.ose.tasks.dto.material;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;


public class FMaterialStocktakeSearchDTO extends PageDTO {

    private static final long serialVersionUID = 2067994112085089485L;

    @Schema(description = "区分pc端")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
