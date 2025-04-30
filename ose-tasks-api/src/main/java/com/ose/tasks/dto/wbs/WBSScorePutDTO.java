package com.ose.tasks.dto.wbs;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * WBS 权重更新数据传输对象。
 */
public class WBSScorePutDTO extends BaseDTO {

    private static final long serialVersionUID = -97599879309194541L;

    @Schema(description = "权重值")
    @NotNull
    @Positive
    private Double score;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

}
