package com.ose.tasks.dto.drawing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 批处理执行结果。
 */
public class DrawingImportResultDTO extends BaseDTO {

    private static final long serialVersionUID = -2807499199738656460L;

    // 输入数据总数
    private Integer totalCount = 0;

    // 成功处理件数
    private Integer processedCount = 0;

    // 跳过数据件数
    private Integer skippedCount = 0;

    // 发生错误的数据件数
    private Integer errorCount = 0;

    // 执行进度百分比
    private Integer progress = 0;

    // 日志
    private List<String> log = new ArrayList<>();

    @JsonIgnore
    private List<Long> ids = new ArrayList<>();

    /**
     * 构造方法。
     */
    public DrawingImportResultDTO() {
    }

    /**
     * 构造方法。
     */
    public DrawingImportResultDTO(
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
     * 累加数据处理件数。
     */
    public void sum(DrawingImportResultDTO result) {
        this.totalCount += result.getTotalCount();
        this.processedCount += result.getProcessedCount();
        this.skippedCount += result.getSkippedCount();
        this.errorCount += result.getErrorCount();
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

    public DrawingImportResultDTO addLog(String message) {
        log.add(message);
        return this;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

}
