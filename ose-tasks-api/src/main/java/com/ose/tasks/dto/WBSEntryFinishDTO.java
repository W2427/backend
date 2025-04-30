package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

/**
 * 项目 WBS 层级结构数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WBSEntryFinishDTO extends BaseDTO {

    private static final long serialVersionUID = -8558506157257334310L;

    @Schema(description = "是否通过")
    @NotNull
    private Boolean approved;

    @Schema(description = "实际工时")
    @NotNull
    private Double hours;

    @Schema(description = "是否启动后续任务")
    private Boolean isHalt;


    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public Boolean getHalt() {
        return isHalt;
    }

    public void setHalt(Boolean halt) {
        isHalt = halt;
    }
}
