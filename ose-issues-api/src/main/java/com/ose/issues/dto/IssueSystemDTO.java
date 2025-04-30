package com.ose.issues.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class IssueSystemDTO extends ExperienceUpdateDTO {


    private static final long serialVersionUID = 6978925672492412297L;
    @Schema(description = "问题系统")
    private String system;

    public IssueSystemDTO() {
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }
}
