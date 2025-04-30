package com.ose.auth.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class QueryOrgMemberDTO extends PageDTO {

    private static final long serialVersionUID = -6718718376183672452L;
    @Schema(description = "关键字")
    private String keyword;

    private Boolean cosign = false;

    public Boolean getCosign() {
        return cosign;
    }

    public void setCosign(Boolean cosign) {
        this.cosign = cosign;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
