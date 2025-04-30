package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import com.ose.tasks.vo.wbs.WBSEntryType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 项目 WBS 条目查询数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WBSEntryQueryDTO extends BaseDTO {

    private static final long serialVersionUID = -6911867437586369971L;

    @Schema(description = "条目类型")
    private WBSEntryType entryType;

    @Schema(description = "计划开始时间（yyyy-MM-ddTHH:mm:ss.SSSZ）")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date startAt = null;

    @Schema(description = "计划开始时间（自）（yyyy-MM-ddTHH:mm:ss.SSSZ）")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date startFrom = null;

    @Schema(description = "计划开始时间（至）（yyyy-MM-ddTHH:mm:ss.SSSZ）")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date startTo = null;

    @Schema(description = "计划完成时间（yyyy-MM-ddTHH:mm:ss.SSSZ）")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date finishAt = null;

    @Schema(description = "计划完成时间（自）（yyyy-MM-ddTHH:mm:ss.SSSZ）")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date finishFrom = null;

    @Schema(description = "计划完成时间（至）（yyyy-MM-ddTHH:mm:ss.SSSZ）")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date finishTo = null;

    @Schema(description = "实施班组 ID")
    private Long teamId = null;

    @Schema(description = "施工场地 ID")
    private Long workSiteId = null;

    @Schema(description = "模块类型")
    private String moduleType = null;

    @Schema(description = "所属模块层级节点 ID")
    private Long moduleId = null;

    @Schema(description = "模块编号")
    private String moduleNo = null;

    @Schema(description = "管线编号")
    private String isoNo = null;

    @Schema(description = "工序阶段")
    private String stage = null;

    @Schema(description = "工序")
    private String process = null;

    @Schema(description = "实体编号（前部匹配）")
    private String entityNo = null;

    @Schema(description = "实体类型")
    private String entityType = null;

    @Schema(description = "实体子类型")
    private String entitySubType = null;

    @Schema(description = "是否已启用（默认查询启用的条目）")
    private Boolean active = true;

    @Schema(description = "是否已结束（默认查询全部）")
    private Boolean finished = null;

    @Schema(description = "任务执行状态（默认查询全部）")
    private WBSEntryRunningStatus runningStatus = null;

    @Schema(description = "取得最大层级深度")
    private Integer depth = 0;

    @Schema(description = "任务包 ID")
    private Long taskPackageId;

    @Schema(description = "计划包 ID")
    private Long bundleId;

    public WBSEntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(WBSEntryType entryType) {
        this.entryType = entryType;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(Date startFrom) {
        this.startFrom = startFrom;
    }

    public Date getStartTo() {
        return startTo;
    }

    public void setStartTo(Date startTo) {
        this.startTo = startTo;
    }

    public Date getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(Date finishAt) {
        this.finishAt = finishAt;
    }

    public Date getFinishFrom() {
        return finishFrom;
    }

    public void setFinishFrom(Date finishFrom) {
        this.finishFrom = finishFrom;
    }

    public Date getFinishTo() {
        return finishTo;
    }

    public void setFinishTo(Date finishTo) {
        this.finishTo = finishTo;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getWorkSiteId() {
        return workSiteId;
    }

    public void setWorkSiteId(Long workSiteId) {
        this.workSiteId = workSiteId;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public String getIsoNo() {
        return isoNo;
    }

    public void setIsoNo(String isoNo) {
        this.isoNo = isoNo;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public WBSEntryRunningStatus getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(WBSEntryRunningStatus runningStatus) {
        this.runningStatus = runningStatus;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    public Long getBundleId() {
        return bundleId;
    }

    public void setBundleId(Long bundleId) {
        this.bundleId = bundleId;
    }
}
