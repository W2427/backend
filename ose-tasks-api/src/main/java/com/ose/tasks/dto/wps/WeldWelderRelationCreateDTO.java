package com.ose.tasks.dto.wps;


import com.ose.dto.BaseDTO;
import com.ose.vo.DisciplineCode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class WeldWelderRelationCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -8123087772303727602L;

    @Schema(description = "焊口焊口关系数组")
    private List<WeldWelderRelationCreateItemDTO> weldWelderRelation;

    @Schema(description = "专业")
    private DisciplineCode discipline;

    @Schema(description = "焊接时间")
    private String weldTime;

    public List<WeldWelderRelationCreateItemDTO> getWeldWelderRelation() {
        return weldWelderRelation;
    }

    public void setWeldWelderRelation(List<WeldWelderRelationCreateItemDTO> weldWelderRelation) {
        this.weldWelderRelation = weldWelderRelation;
    }

    public DisciplineCode getDiscipline() {
        return discipline;
    }

    public void setDiscipline(DisciplineCode discipline) {
        this.discipline = discipline;
    }

    public String getWeldTime() {
        return weldTime;
    }

    public void setWeldTime(String weldTime) {
        this.weldTime = weldTime;
    }
}
