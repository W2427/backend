package com.ose.issues.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class IssueModuleDTO extends ExperienceUpdateDTO {


    private static final long serialVersionUID = 6781275698095045220L;
    @Schema(description = "问题模块")
    private String module;

    public IssueModuleDTO() {
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
