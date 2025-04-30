package com.ose.tasks.dto.taskpackage;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 任务包查询条件数据传输对象。
 */
public class TaskPackageAUthCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = 5814648750747221108L;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "id")
    private Long id;

    public TaskPackageAUthCriteriaDTO() {
    }

    public TaskPackageAUthCriteriaDTO(String name, Long id) {
        this.name = name;
        this.id = id;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
