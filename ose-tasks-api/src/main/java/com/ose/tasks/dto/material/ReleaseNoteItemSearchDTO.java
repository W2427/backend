package com.ose.tasks.dto.material;

import com.ose.dto.PageDTO;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 放行单查询DTO
 */
public class ReleaseNoteItemSearchDTO extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "IDENT码")
    private String ident;

    public String getIdent() {
        return StringUtils.isEmpty(ident) ? "" : ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }
}
