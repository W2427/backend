package com.ose.issues.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class IssueSourceDTO extends ExperienceUpdateDTO {

    private static final long serialVersionUID = 5525504863323681950L;

    @Schema(description = "问题来源")
    private String source;

    public IssueSourceDTO() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
