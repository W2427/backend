package com.ose.tasks.dto.bpm;


import com.ose.dto.BaseDTO;
import com.ose.vo.DisciplineCode;
import io.swagger.v3.oas.annotations.media.Schema;

public class DeliverySearchConditionDTO extends BaseDTO {

    private static final long serialVersionUID = -8850581721091004866L;

    @Schema(description = "专业")
    private DisciplineCode discipline;

    public DisciplineCode getDiscipline() {
        return discipline;
    }

    public void setDiscipline(DisciplineCode discipline) {
        this.discipline = discipline;
    }
}
