package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 流程对应外检安排 DTO
 */
public class BpmExInspScheduleInstIdDTO extends BaseDTO {

    private static final long serialVersionUID = 5971440931157335574L;

    @Schema(description = "流程实例Id")
    private Long actInstId;

    @Schema(description = "状态")
    private String status;

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
