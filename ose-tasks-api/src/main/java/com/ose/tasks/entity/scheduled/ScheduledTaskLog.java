package com.ose.tasks.entity.scheduled;

import com.ose.entity.BaseEntity;
import com.ose.tasks.vo.scheduled.ScheduledTaskCode;
import com.ose.tasks.vo.scheduled.ScheduledTaskStatus;
import com.ose.tasks.vo.scheduled.ScheduledTaskType;
import com.ose.util.RemoteAddressUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 定时任务记录数据实体。
 */
@Entity
@Table(name = "schedule_log")
public class ScheduledTaskLog extends BaseEntity {

    private static final long serialVersionUID = 356033145181417740L;

    @Schema(description = "项目 ID")
    @Column(length = 16)
    private Long projectId;

    @Schema(description = "定时任务类型")
    @Column(length = 45, nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduledTaskType type;

    @Schema(description = "业务代码")
    @Column(length = 45, nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduledTaskCode code;

    @Schema(description = "任务名称")
    @Column(length = 45, nullable = false)
    private String name;

    @Schema(description = "输入总数")
    @Column
    private Integer totalCount = 0;

    @Schema(description = "处理件数")
    @Column
    private Integer processedCount = 0;

    @Schema(description = "忽略件数")
    @Column
    private Integer passedCount = 0;

    @Schema(description = "错误件数")
    @Column
    private Integer errorCount = 0;

    @Schema(description = "开始时间")
    @Column(nullable = false)
    private Date startedAt;

    @Schema(description = "最后更新时间")
    private Date lastModifiedAt;

    @Schema(description = "完成时间")
    @Column
    private Date finishedAt;

    @Schema(description = "中止时间")
    @Column
    private Date stoppedAt;

    @Schema(description = "中止操作者 ID")
    @Column
    private Long stoppedBy;

    @Schema(description = "错误日志")
    @Column(length = 512)
    private String errorLog;

    @Schema(description = "服务器主机信息")
    @Column
    private String hostname = RemoteAddressUtils.getHostLabel();

    @Schema(description = "定时任务状态")
    @Column(length = 45, nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduledTaskStatus status;

    public ScheduledTaskLog() {
        setStartedAt(new Date());
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public ScheduledTaskType getType() {
        return type;
    }

    public void setType(ScheduledTaskType type) {
        this.type = type;
    }

    public ScheduledTaskCode getCode() {
        return code;
    }

    public void setCode(ScheduledTaskCode code) {
        this.code = code;
        this.type = code.getType();
        this.name = code.getDisplayName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getProcessedCount() {
        return processedCount;
    }

    public void setProcessedCount(Integer processedCount) {
        this.processedCount = processedCount;
    }

    public Integer getPassedCount() {
        return passedCount;
    }

    public void setPassedCount(Integer passedCount) {
        this.passedCount = passedCount;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Date getStoppedAt() {
        return stoppedAt;
    }

    public void setStoppedAt(Date stoppedAt) {
        this.stoppedAt = stoppedAt;
    }

    public Long getStoppedBy() {
        return stoppedBy;
    }

    public void setStoppedBy(Long stoppedBy) {
        this.stoppedBy = stoppedBy;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }

    public ScheduledTaskStatus getStatus() {
        return status;
    }

    public void setStatus(ScheduledTaskStatus status) {
        this.status = status;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt == null ? startedAt : lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
