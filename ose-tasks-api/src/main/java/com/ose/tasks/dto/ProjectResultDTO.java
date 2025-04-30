package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class ProjectResultDTO extends BaseDTO {

    private static final long serialVersionUID = 4597709308833365375L;
    @Schema(description = "项目号")
    private String name;

    @Schema(description = "工时填写上限")
    private Integer dayWorkHour;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDayWorkHour() {
        return dayWorkHour;
    }

    public void setDayWorkHour(Integer dayWorkHour) {
        this.dayWorkHour = dayWorkHour;
    }
}
