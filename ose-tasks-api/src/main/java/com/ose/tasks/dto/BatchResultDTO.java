package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.BatchTask;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * 批处理执行结果。
 */
public class BatchResultDTO extends BaseDTO {

    private static final long serialVersionUID = -2807499199738656460L;

    @Schema(description = "批处理任务 ID BSTKSDF39")
    private Long taskId;

    @Schema(description = "批处理任务实例")
    private BatchTask batchTask;

    @Schema(description = "输入数据总数")
    private Integer totalCount = 0;

    @Schema(description = "成功处理件数")
    private Integer processedCount = 0;


    @Schema(description = "成功处理关系数")
    private Integer processedRelationCount = 0;

    @Schema(description = "跳过数据件数")
    private Integer skippedCount = 0;

    @Schema(description = "发生错误的数据件数")
    private Integer errorCount = 0;

    @Schema(description = "执行进度百分比")
    private Integer progress = 0;

    @Schema(description = "日志")
    private List<String> log = new ArrayList<>();

    /**
     * 构造方法。
     */
    public BatchResultDTO() {
    }

    /**
     * 构造方法。
     */
    public BatchResultDTO(BatchTask batchTask) {
        this.taskId = batchTask.getId();
        this.batchTask = batchTask;
    }

    /**
     * 构造方法。
     */
    public BatchResultDTO(
        int totalCount,
        int processedCount,
        int skippedCount,
        int errorCount
    ) {
        this.totalCount = totalCount;
        this.processedCount = processedCount;
        this.skippedCount = skippedCount;
        this.errorCount = errorCount;
    }

    /**
     * 构造方法。
     */
    public BatchResultDTO(
        int totalCount,
        int processedCount,
        int processedRelationCount,
        int skippedCount,
        int errorCount
    ) {
        this.totalCount = totalCount;
        this.processedCount = processedCount;
        this.processedRelationCount = processedRelationCount;
        this.skippedCount = skippedCount;
        this.errorCount = errorCount;
    }

    /**
     * 累加数据处理件数。
     */
    public void sum(BatchResultDTO result) {
        this.totalCount += result.getTotalCount();
        this.processedCount += result.getProcessedCount();
        this.skippedCount += result.getSkippedCount();
        this.errorCount += result.getErrorCount();
    }

    public Long getTaskId() {
        return taskId;
    }

    public BatchTask getBatchTask() {
        return batchTask;
    }

    public void addTotalCount(int totalCount) {
        this.totalCount += totalCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void addProcessedCount(int processedCount) {
        this.processedCount += processedCount;
        updateProgress();
    }

    public Integer getProcessedRelationCount() {
        return processedRelationCount;
    }

    public void addProcessedRelationCount(int processedRelationCount) {
        this.processedRelationCount += processedRelationCount;
    }

    public Integer getProcessedCount() {
        return processedCount;
    }

    public void addSkippedCount(int skippedCount) {
        this.skippedCount += skippedCount;
        updateProgress();
    }

    public Integer getSkippedCount() {
        return skippedCount;
    }

    public void addErrorCount(int errorCount) {
        this.errorCount += errorCount;
        updateProgress();
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void updateProgress() {

        if (totalCount == 0) {
            return;
        }

        progress = Math.round(
            (processedCount + skippedCount + errorCount) * 100f / totalCount
        );

    }

    public Integer getProgress() {
        return progress;
    }

    public List<String> getLog() {
        return log;
    }

    public void setLog(List<String> log) {
        this.log = log;
    }

    public BatchResultDTO addLog(String message) {
        log.add(message);
        return this;
    }

}
