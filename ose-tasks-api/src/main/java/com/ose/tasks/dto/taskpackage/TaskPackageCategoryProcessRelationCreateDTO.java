package com.ose.tasks.dto.taskpackage;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 任务包类型-工序创建数据传输对象。
 */
public class TaskPackageCategoryProcessRelationCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 6718331978041993340L;

    @Schema(description = "名称")
    private Long processId;

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

}
