package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

public class OrganizationIDCDTO extends BaseDTO {

    private static final long serialVersionUID = 1614804161609134665L;

    @Schema(description = "层级节点列表")
    private List<OrganizationIDCDetailDTO> children = new ArrayList<>();

    public List<OrganizationIDCDetailDTO> getChildren() {
        return children;
    }

    public void setChildren(List<OrganizationIDCDetailDTO> children) {
        this.children = children;
    }
}
