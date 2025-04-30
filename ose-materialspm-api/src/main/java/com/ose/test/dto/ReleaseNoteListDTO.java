package com.ose.test.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 放行单 查询DTO
 */
public class ReleaseNoteListDTO extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "SPM 放行单号")
    private String spmRelnNumber = "";

    public String getSpmRelnNumber() {
        return spmRelnNumber;
    }

    public void setSpmRelnNumber(String spmRelnNumber) {
        this.spmRelnNumber = spmRelnNumber;
    }
}
