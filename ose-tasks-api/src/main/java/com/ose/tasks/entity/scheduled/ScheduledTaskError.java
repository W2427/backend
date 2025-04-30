package com.ose.tasks.entity.scheduled;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * 定时任务记录数据实体。
 */
@Entity
@Table(name = "schedule_error")
public class ScheduledTaskError extends BaseEntity {

    private static final long serialVersionUID = -8613625775849024615L;

    @Schema(description = "定时任务日志 ID")
    @Column(nullable = false)
    private Long logId;

    @Schema(description = "处理的数据的 ID")
    @Column
    private Long itemId;

    @Schema(description = "错误类型")
    @Column
    private String errorType;

    @Schema(description = "错误信息")
    @Lob
    @Column
    private String error;

    @Schema(description = "记录创建时间")
    @Column
    private Date createdAt = new Date();

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
