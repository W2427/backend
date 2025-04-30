package com.ose.auth.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class QueryRoleMemberDTO extends PageDTO {

    private static final long serialVersionUID = -4110904482911428450L;

    @Schema(description = "关键字")
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
