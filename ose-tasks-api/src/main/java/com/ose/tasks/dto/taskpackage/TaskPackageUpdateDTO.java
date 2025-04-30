package com.ose.tasks.dto.taskpackage;

import com.ose.annotation.NullableNotBlank;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Size;

/**
 * 任务包更新数据传输对象。
 */
public class TaskPackageUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = -7446092956439307900L;

    @Schema(description = "名称")
    @NullableNotBlank
    @Size(max = 255)
    private String name;

    @Schema(description = "分类 ID")
    private Long categoryId;

    @Schema(description = "排序编号")
    private Integer sortOrder;

    @Schema(description = "备注")
    @Size(max = 500)
    private String memo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setEntitySubTypeId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}
