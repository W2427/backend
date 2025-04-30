package com.ose.tasks.dto.rapairData;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 修改项目数据接口。
 */
public class RepairBatchRevocationNodeDTO extends BaseDTO {

    private static final long serialVersionUID = 7537633537478155941L;

    @Schema(description = "流程id")
    private Long actInstId;

    @Schema(description = "撤回节点名称")
    private String taskDefKey;

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }
}
