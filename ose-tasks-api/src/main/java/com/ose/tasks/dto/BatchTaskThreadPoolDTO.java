package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 查询各个线程池中的参数。
 */
public class BatchTaskThreadPoolDTO extends BaseDTO {

    private static final long serialVersionUID = -21761137869832380L;

    @Schema(description = "线程池名称")
    private String name;

    @Schema(description = "线程池中正在执行的数量")
    private Integer queueSize = 0;

    @Schema(description = "线程池中线程的数量")
    private Integer activeCount = 0;

    @Schema(description = "线程池中队列的数量")
    private long completedTaskCount = 0;

    @Schema(description = "线程池中队列的数量")
    private long taskCount = 0;

    @Schema(description = "池中所保存的线程数，包括空闲线程")
    private long corePoolSize = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public Integer getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(Integer activeCount) {
        this.activeCount = activeCount;
    }

    public long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(long taskCount) {
        this.taskCount = taskCount;
    }

    public long getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(long corePoolSize) {
        this.corePoolSize = corePoolSize;
    }
}
