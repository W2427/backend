package com.ose.tasks.dto.wbs;

import com.ose.dto.BaseDTO;

/**
 * WBS 三级计划的直接子条目统计信息数据传输对象。
 */
public class WBSEntryChildStatisticsDTO extends BaseDTO {

    private static final long serialVersionUID = -2130577784452158286L;

    // 子条目总数
    private long count;

    // 子条目权重总和
    private double totalScore;

    // 子条目已完成权重总和
    private double finishedScore;

    // 实际工时
    private double actualDuration;

    /**
     * 构造方法。
     *
     * @param count         子条目总数
     * @param totalScore    子条目权重总和
     * @param finishedScore 子条目已完成权重总和
     */
    public WBSEntryChildStatisticsDTO(Long count, Double totalScore, Double finishedScore, Double actualDuration) {
        this.count = count == null ? 0 : count;
        this.totalScore = totalScore == null ? 0 : totalScore;
        this.finishedScore = finishedScore == null ? 0 : finishedScore;
        this.actualDuration = actualDuration == null ? 0 : actualDuration;
    }

    public long getCount() {
        return count;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public double getFinishedScore() {
        return finishedScore;
    }

    public double getActualDuration() {
        return actualDuration;
    }

}
