package com.ose.tasks.dto.wbs;

import com.ose.dto.PageDTO;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 四级计划查询条件数据传输对象。
 */
public class WBSEntryCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -6788202608785109005L;

    @Schema(description = "模块编号")
    private String moduleNo;

    @Schema(description = "ISO 编号")
    private String isoNo;

    @Schema(description = "工序阶段名称")
    private String stageName;

    @Schema(description = "工序名称")
    private String processName;

    @Schema(description = "分段")
    private String sector;

    @Schema(description = "实体类型")
    private String entityType;

    @Schema(description = "实体子类型")
    private String entitySubType;

    @Schema(description = "任务包 ID")
    private Long taskPackageId;

    @Schema(description = "工作场地 ID")
    private Long workSiteId;

    @Schema(description = "工作组 ID")
    private Long teamId;

    @Schema(description = "是否分页，默认true")
    private boolean pageable = false;

    @Schema(description = "运行状态 running_status")
    private WBSEntryRunningStatus runningStatus;

    @Schema(description = "实体编号")
    private String entityNo;

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
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

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
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

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    public Long getWorkSiteId() {
        return workSiteId;
    }

    public void setWorkSiteId(Long workSiteId) {
        this.workSiteId = workSiteId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public WBSEntryRunningStatus getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(WBSEntryRunningStatus runningStatus) {
        this.runningStatus = runningStatus;
    }

    public boolean isPageable() {
        return pageable;
    }

    public void setPageable(boolean pageable) {
        this.pageable = pageable;
    }
}
