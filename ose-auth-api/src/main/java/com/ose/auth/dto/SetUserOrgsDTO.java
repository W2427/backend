package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class SetUserOrgsDTO extends BaseDTO {

    private static final long serialVersionUID = 3422302213399678127L;

    @Schema(description = "用户组织ID列表")
    private List<String> orgs;

    public List<String> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<String> orgs) {
        this.orgs = orgs;
    }
}
