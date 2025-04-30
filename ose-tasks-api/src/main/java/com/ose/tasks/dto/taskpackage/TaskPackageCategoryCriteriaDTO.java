package com.ose.tasks.dto.taskpackage;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 任务包类型查询条件数据传输对象。
 */
public class TaskPackageCategoryCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = 4211553754658157522L;

    @Schema(description = "查询关键字")
    private String keyword;

    @Schema(description = "实体类型")
    private String entityType;

    @Schema(description = "专业")
    private String discipline;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
