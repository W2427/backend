package com.ose.tasks.dto.wbs;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 启动对应工序四级计划及任务。
 */
public class WBSEntryWeldPlainStartDTO extends BaseDTO {

    private static final long serialVersionUID = -5918818138059309775L;

    @Schema(description = "工序id数组")
    private List<Long> entityIds ;

    public List<Long> getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(List<Long> entityIds) {
        this.entityIds = entityIds;
    }
}
