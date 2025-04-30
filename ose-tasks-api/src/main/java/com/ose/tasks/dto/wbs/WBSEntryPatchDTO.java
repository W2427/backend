package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * 项目 WBS 条目更新数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WBSEntryPatchDTO extends BaseDTO {

    private static final long serialVersionUID = -130928603321316193L;

    @Schema(description = "权重")
    private Double score;

    @Schema(description = "开始时间")
    private Date startAt;

    @Schema(description = "截止时间")
    private Date finishAt;

    @Schema(description = "工时（小时）")
    private Double duration;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "是否启用")
    private Boolean active;

    @Schema(description = "四级计划自动启动提前时长（单位：小时）")
    private Integer startBeforeHours;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(Date finishAt) {
        this.finishAt = finishAt;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getStartBeforeHours() {
        return startBeforeHours;
    }

    public void setStartBeforeHours(Integer startBeforeHours) {
        this.startBeforeHours = startBeforeHours;
    }

}
