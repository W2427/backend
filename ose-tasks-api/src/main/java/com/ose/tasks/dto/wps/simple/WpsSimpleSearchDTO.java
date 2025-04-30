package com.ose.tasks.dto.wps.simple;


import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class WpsSimpleSearchDTO extends PageDTO {

    private static final long serialVersionUID = -156631690277045208L;

    @Schema(description = "关键字")
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
