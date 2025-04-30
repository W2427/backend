package com.ose.material.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.entity.BaseEntity;
import com.ose.exception.BusinessError;
import com.ose.material.dto.MmImportBatchResultDTO;
import com.ose.material.vo.MaterialImportType;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.util.DateUtils;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 批处理任务数据实体。
 */
@MappedSuperclass
public abstract class MmImportBatchTaskBase extends BaseEntity {

    private static final long serialVersionUID = -8571116564519707266L;

    @Schema(description = "组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column(length = 16)
    private Long projectId;

    @Schema(description = "实体编号")
    @Column(length = 16)
    private String entityNo;

    @Schema(description = "实体 ID")
    @Column(length = 16)
    private Long entityId;

    @Schema(description = "材料导入类型代码")
    @Column(nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private MaterialImportType code;

    @Schema(description = "任务名称")
    @Column(nullable = false, length = 255)
    private String name;

    @Schema(description = "开始时间")
    @Column(nullable = false)
    private Date startAt;

    @Schema(description = "最后更新时间")
    @Column
    private Date lastModifiedAt;

    @Schema(description = "结束时间")
    @Column
    private Date finishedAt;

    @Schema(description = "启动者 ID")
    @Column(nullable = false)
    private Long launchedBy;

    @Schema(description = "结束者 ID")
    @Column
    private Long stoppedBy;

    @Schema(description = "输入数据总数")
    @Column
    private Integer totalCount;

    @Schema(description = "成功处理数据件数")
    @Column
    private Integer processedCount;

    @Schema(description = "成功处理关系数据件数")
    @Column
    private Integer processedRelationCount;

    @Schema(description = "跳过数据件数")
    @Column
    private Integer skippedCount;

    @Schema(description = "发生错误的数据件数")
    @Column
    private Integer errorCount;

    @Schema(description = "导入文件 ID")
    @Column
    private Long importFile;

    @Schema(description = "当前处理描述")
    @Column(length = 4096)
    private String progressText = "";

    @Schema(description = "是否正在运行")
    @Column(nullable = false)
    private boolean running = true;

    @Schema(description = "执行状态")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    @Schema(description = "仓库类型（公司、项目）")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    public MaterialOrganizationType materialOrganizationType;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public MaterialImportType getCode() {
        return code;
    }

    public void setCode(MaterialImportType code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    @JsonIgnore
    public Long getLaunchedBy() {
        return launchedBy;
    }

    public void setLaunchedBy(Long launchedBy) {
        this.launchedBy = launchedBy;
    }

    @JsonIgnore
    public Long getStoppedBy() {
        return stoppedBy;
    }

    public void setStoppedBy(Long stoppedBy) {
        this.stoppedBy = stoppedBy;
    }

    public boolean checkRunningStatus() {
        if (getStatus() == EntityStatus.CLOSED) {
            throw new BusinessError("error.batch-task.stopped");
        }
        return true;
    }

    public void setResult(MmImportBatchResultDTO result) {

        if (result == null) {
            return;
        }

        setTotalCount(result.getTotalCount());
        setProcessedCount(result.getProcessedCount());
        setProcessedRelationCount(result.getProcessedRelationCount());
        setSkippedCount(result.getSkippedCount());
        setErrorCount(result.getErrorCount());
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        checkRunningStatus();
        this.totalCount = totalCount;
    }

    public Integer getProcessedCount() {
        return processedCount;
    }

    public void setProcessedCount(Integer processedCount) {
        checkRunningStatus();
        this.processedCount = processedCount;
    }

    public Integer getProcessedRelationCount() {
        return processedRelationCount;
    }

    public void setProcessedRelationCount(Integer processedRelationCount) {
        this.processedRelationCount = processedRelationCount;
    }

    public Integer getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(Integer skippedCount) {
        checkRunningStatus();
        this.skippedCount = skippedCount;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        checkRunningStatus();
        this.errorCount = errorCount;
    }

    public Long getImportFile() {
        return importFile;
    }

    public void setImportFile(Long importFile) {
        this.importFile = importFile;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        if (this.status == EntityStatus.CLOSED) {
            return;
        }
        this.status = status;
    }


    /**
     * 取得启动者引用信息。
     *
     * @return 启动者引用信息
     */
    @JsonProperty(value = "launchedBy", access = READ_ONLY)
    public ReferenceData getLaunchedByRef() {
        return this.launchedBy == null
            ? null
            : new ReferenceData(this.launchedBy);
    }

    /**
     * 取得停止者引用信息。
     *
     * @return 停止者引用信息
     */
    @JsonProperty(value = "stoppedBy", access = READ_ONLY)
    public ReferenceData getStoppedByRef() {
        return this.stoppedBy == null
            ? null
            : new ReferenceData(this.stoppedBy);
    }

    @Override
    public Set<Long> relatedUserIDs() {

        Set<Long> userIDs = new HashSet<>();

        if (this.getLaunchedBy() != null && this.getLaunchedBy() != 0L) {
            userIDs.add(this.getLaunchedBy());
        }

        if (this.getStoppedBy() != null && this.getStoppedBy() != 0L) {
            userIDs.add(this.getStoppedBy());
        }

        return userIDs;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt() {
        setLastModifiedAt(new Date());
    }

    @JsonSetter
    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getProgressText() {
        return progressText;
    }

    public void setProgressText(String progressText) {
        setLastModifiedAt();
        this.progressText += String.format(
            "[%s] %s\r\n",
            DateUtils.toISOString(lastModifiedAt),
            progressText
        );

    }

    public MaterialOrganizationType getWareHouseType() {
        return materialOrganizationType;
    }

    public void setWareHouseType(MaterialOrganizationType materialOrganizationType) {
        this.materialOrganizationType = materialOrganizationType;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
