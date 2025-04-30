package com.ose.tasks.dto.taskpackage;

import com.ose.annotation.NullableNotBlank;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Size;

/**
 * 任务包类型更新数据传输对象。
 */
public class TaskPackageCategoryUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = 4211553754658157522L;

    @Schema(description = "名称")
    @NullableNotBlank
    @Size(max = 255)
    private String name;

    @Schema(description = "描述")
    @NullableNotBlank
    @Size(max = 255)
    private String description;

    @Schema(description = "实体类型")
    private String entityType;

    @Schema(description = "专业")
    private String discipline;

    @Schema(description = "备注")
    @Size(max = 500)
    private String memo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
