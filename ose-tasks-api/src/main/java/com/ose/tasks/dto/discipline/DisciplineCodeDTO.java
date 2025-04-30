package com.ose.tasks.dto.discipline;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Discipline创建数据传输对象。
 */
public class DisciplineCodeDTO extends BaseDTO {

    private static final long serialVersionUID = 3974322748227496364L;
    @Schema(description = "专业代码")
    private String discipline;

    @Schema(description = "专业说明")
    private String description;

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
