package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 任务查询条件数据传输对象类。
 */
public class TaskAssigneHistoryDTO extends BaseDTO {

    private static final long serialVersionUID = -7965308215271373880L;
    /**
     *
     */

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "任务id")
    private Long actInstId;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }
}
