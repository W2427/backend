package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class UserProjectDTO extends BaseDTO {

    private static final long serialVersionUID = 3587669617392545462L;

    @Schema(description = "项目名")
    private String name;

    @Schema(description = "项目Id")
    private Long projectId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
