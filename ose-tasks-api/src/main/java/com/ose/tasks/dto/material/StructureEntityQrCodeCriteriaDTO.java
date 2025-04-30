package com.ose.tasks.dto.material;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 */
public class StructureEntityQrCodeCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = 4202577050195856645L;

    @Schema(description = "关键字")
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
