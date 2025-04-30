package com.ose.tasks.dto.taskpackage;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 任务包创建数据传输对象。
 */
public class TaskPackageCreateDTO extends TaskPackageUpdateDTO {

    private static final long serialVersionUID = 8023953711988418931L;

    @Schema(description = "名称")
    @NotBlank
    @Size(max = 255)
    private String name;

    @Schema(description = "分类 ID")
    private Long categoryId;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getCategoryId() {
        return categoryId;
    }

    @Override
    public void setEntitySubTypeId(Long categoryId) {
        this.categoryId = categoryId;
    }

}
