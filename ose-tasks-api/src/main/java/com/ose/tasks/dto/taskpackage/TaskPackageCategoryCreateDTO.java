package com.ose.tasks.dto.taskpackage;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 任务包类型创建数据传输对象。
 */
public class TaskPackageCategoryCreateDTO extends TaskPackageCategoryUpdateDTO {

    private static final long serialVersionUID = -8021765645918359352L;

    @Schema(description = "名称")
    @NotBlank
    @Size(max = 255)
    private String name;

    @Schema(description = "描述")
    @NotBlank
    @Size(max = 255)
    private String description;

    @Schema(description = "实体类型")
    @NotNull
    private String entityType;

    @Schema(description = "功能分区")
    @NotNull
    private String funcPart;

    @Schema(description = "专业")
    @NotNull
    private String discipline;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getEntityType() {
        return entityType;
    }

    @Override
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }
}
