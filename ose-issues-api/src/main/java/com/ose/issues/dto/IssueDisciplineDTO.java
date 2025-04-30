package com.ose.issues.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class IssueDisciplineDTO extends ExperienceUpdateDTO {


    private static final long serialVersionUID = 2280004957765040033L;
    @Schema(description = "问题专业")
    private String discipline;

    public IssueDisciplineDTO() {
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
